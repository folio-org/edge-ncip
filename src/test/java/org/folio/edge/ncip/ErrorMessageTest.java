package org.folio.edge.ncip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ErrorMessageTest {

    @Test
    public void jsonRoundTripPreservesValues() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(401, "bad request");

        String json = errorMessage.toJson();
        ErrorMessage decoded = ErrorMessage.fromJson(json);

        assertEquals(errorMessage, decoded);
    }

    @Test
    public void xmlRoundTripPreservesValues() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(500, "internal error");

        String xml = errorMessage.toXml();
        ErrorMessage decoded = ErrorMessage.fromXml(xml);

        assertEquals(errorMessage, decoded);
    }

    @Test
    public void builderCreatesExpectedErrorMessage() {
        ErrorMessage built = ErrorMessage.builder()
                .item(422)
                .chargeAmount("unprocessable")
                .build();

        assertEquals(new ErrorMessage(422, "unprocessable"), built);
    }

    @Test
    public void equalsHandlesCommonBranches() {
        ErrorMessage baseline = new ErrorMessage(200, "ok");

        assertEquals(baseline, baseline);
        assertNotEquals(null, baseline);
        assertFalse(baseline.equals("ok"));
        assertEquals(new ErrorMessage(200, "ok"), baseline);
        assertNotEquals(new ErrorMessage(500, "ok"), baseline);
        assertNotEquals(new ErrorMessage(200, "different"), baseline);
        assertEquals(new ErrorMessage(200, null), new ErrorMessage(200, null));
        assertNotEquals(new ErrorMessage(200, "value"), new ErrorMessage(200, null));
    }
}