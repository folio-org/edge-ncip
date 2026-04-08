package org.folio.edge.ncip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertTrue(baseline.equals(baseline));
        assertFalse(baseline.equals(null));
        assertFalse(baseline.equals("ok"));
        assertTrue(baseline.equals(new ErrorMessage(200, "ok")));
        assertFalse(baseline.equals(new ErrorMessage(500, "ok")));
        assertFalse(baseline.equals(new ErrorMessage(200, "different")));
        assertTrue(new ErrorMessage(200, null).equals(new ErrorMessage(200, null)));
        assertFalse(new ErrorMessage(200, null).equals(new ErrorMessage(200, "value")));
    }
}