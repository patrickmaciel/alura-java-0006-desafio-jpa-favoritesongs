package info.patrickmaciel.favoriteSongs.favoriteSongs.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.Artist;
import info.patrickmaciel.favoriteSongs.favoriteSongs.model.ArtistType;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConsultaChatGpt {
  private final OpenAiService openAiService;
  private static final String OLD_MODEL = "gpt-3.5-turbo-instruct";
  private static final String NEW_MODEL = "gpt-4o-mini";
  private static final int MAX_TOKENS = 1000;
  private static final double TEMPERATURE = 0.7;
  private List<ChatMessage> conversationHistory;

  public ConsultaChatGpt() {
    this.openAiService = new OpenAiService(loadApiKey());
    this.conversationHistory = new ArrayList<>();
  }

  private String loadApiKey() {
    Properties properties = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new RuntimeException("config.properties not found");
      }
      properties.load(input);
      return properties.getProperty("openAiApiKey");
    } catch (IOException e) {
      throw new RuntimeException("Error loading API key", e);
    }
  }

  public String sendMessage(String message) {
    conversationHistory.add(new ChatMessage(ChatMessageRole.USER.value(), message));

    ChatCompletionRequest request = ChatCompletionRequest.builder()
        .model(NEW_MODEL)
        .messages(conversationHistory)
        .build();

    ChatMessage response = openAiService.createChatCompletion(request)
        .getChoices().get(0).getMessage();

    conversationHistory.add(response);
    return response.getContent();
  }

  public List<String> getConversationHistory() {
    List<String> messages = new ArrayList<>();
    for (ChatMessage message : conversationHistory) {
      messages.add(message.getContent());
    }
    return messages;
  }

  public void clearConversation() {
    conversationHistory.clear();
  }

  private String createCompletion(String prompt) {
    CompletionRequest request = CompletionRequest.builder()
        .model(OLD_MODEL)
        .prompt(prompt)
        .maxTokens(MAX_TOKENS)
        .temperature(TEMPERATURE)
        .build();

    CompletionResult response = openAiService.createCompletion(request);
    return response.getChoices().get(0).getText().trim();
  }

  public Optional<Artist> getArtistData(String artistName) {
    String isGospelResponse = createCompletion(
        String.format("Existe um artista cristão/gospel chamado %s? Responda sim ou não.", artistName));

    if (!isGospelResponse.equalsIgnoreCase("Sim")) {
      return Optional.ofNullable(Artist.builder()
          .isGospelArtist(false)
          .build());
    }

    String artistType = createCompletion(
        String.format("O artista %s é um artista solo, dupla ou banda? Responda apenas solo, dupla ou banda.", artistName));

    String extraData = createCompletion(
        String.format("Me retorne no seguinte formato, as informações desta da banda %s: Ano de formação, Gênero, Quantidade de Albuns, Quantidade músicas. Exemplo: 1997, Gospel, 30, 280", artistName));

    String[] extraDataParts = extraData.split(",");
    return Optional.ofNullable(Artist.builder()
        .isGospelArtist(true)
        .type(ArtistType.fromString(artistType.trim().replace(".", "")))
        .releaseDate(Integer.parseInt(extraDataParts[0].trim()))
        .genre(extraDataParts[1].trim())
        .albumCount(Integer.parseInt(extraDataParts[2].trim()))
        .songCount(Integer.parseInt(extraDataParts[3].trim()))
        .build());
  }
}