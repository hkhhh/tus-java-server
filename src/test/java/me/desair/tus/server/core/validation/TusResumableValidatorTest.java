package me.desair.tus.server.core.validation;

import me.desair.tus.server.HttpHeader;
import me.desair.tus.server.HttpMethod;
import me.desair.tus.server.core.validation.TusResumableValidator;
import me.desair.tus.server.exception.InvalidTusResumableException;
import me.desair.tus.server.upload.UploadIdFactory;
import me.desair.tus.server.upload.UploadStorageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class TusResumableValidatorTest {

    private MockHttpServletRequest servletRequest;
    private TusResumableValidator validator;
    private UploadStorageService uploadStorageService;
    private UploadIdFactory idFactory;

    @Before
    public void setUp() {
        servletRequest = new MockHttpServletRequest();
        validator = new TusResumableValidator();
    }

    @Test(expected = InvalidTusResumableException.class)
    public void validateNoVersion() throws Exception {
        validator.validate(HttpMethod.POST, servletRequest, uploadStorageService, idFactory);
    }

    @Test(expected = InvalidTusResumableException.class)
    public void validateInvalidVersion() throws Exception {
        servletRequest.addHeader(HttpHeader.TUS_RESUMABLE, "2.0.0");
        validator.validate(HttpMethod.POST, servletRequest, uploadStorageService, idFactory);
    }

    @Test
    public void validateOptions() throws Exception {
        servletRequest.addHeader(HttpHeader.TUS_RESUMABLE, "2.0.0");
        validator.validate(HttpMethod.OPTIONS, servletRequest, uploadStorageService, idFactory);
    }

    @Test
    public void validateValid() throws Exception {
        servletRequest.addHeader(HttpHeader.TUS_RESUMABLE, "1.0.0");
        validator.validate(HttpMethod.POST, servletRequest, uploadStorageService, idFactory);
    }

    @Test
    public void validateNullMethod() throws Exception {
        servletRequest.addHeader(HttpHeader.TUS_RESUMABLE, "1.0.0");
        validator.validate(null, servletRequest, uploadStorageService, idFactory);
    }
}