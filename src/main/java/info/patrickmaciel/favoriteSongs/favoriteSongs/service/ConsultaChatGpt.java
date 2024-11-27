package info.patrickmaciel.favoriteSongs.favoriteSongs.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.io.FileInputStream;
import java.util.Properties;

public class ConsultaChatGpt {
  public static String obterTraducao(String texto) {
    Properties configProperties = new Properties();
    String openAiApiKey;

    try {
      configProperties.load(new FileInputStream("config.properties"));
      openAiApiKey = configProperties.getProperty("openAiApiKey");
//      configProperties.load(ConsultaChatGPT.class.getResourceAsStream("/config.properties"));
    } catch (Exception e) {
      throw new RuntimeException("Erro ao carregar arquivo de configuração", e);
    }

    OpenAiService service = new OpenAiService(openAiApiKey);

    CompletionRequest requisicao = CompletionRequest.builder()
        .model("gpt-4o-mini")
        .prompt("traduza para o português o texto: " + texto)
        .maxTokens(1000)
        .temperature(0.7)
        .build();

    var resposta = service.createCompletion(requisicao);
    return resposta.getChoices().get(0).getText();
  }
}
