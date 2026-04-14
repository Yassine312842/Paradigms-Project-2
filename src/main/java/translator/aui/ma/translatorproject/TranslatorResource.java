package translator.aui.ma.translatorproject;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/translate")
public class TranslatorResource {

    private final TranslationService translationService = new TranslationService();

    @POST
    @Consumes("text/plain; charset=UTF-8")
    public Response translate(String text) {
        String translated = translationService.translateToDarija(text);
        TranslationResponse response = new TranslationResponse(text, translated);

        return Response.ok(response)
                .type("application/json; charset=UTF-8")
                .build();
    }
}