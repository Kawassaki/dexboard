package br.com.dextra.dexboard.component;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import java.io.Closeable;
import java.io.IOException;

class LocalGaeEnvironment implements Closeable {

    private final LocalServiceTestHelper gaeHelper;

    private LocalGaeEnvironment() {
        this.gaeHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
                .setEnvIsAdmin(true)
                .setEnvIsLoggedIn(true)
                .setEnvEmail("test@dextra-sw.com")
                .setEnvAuthDomain("dextra-sw.com");
    }

    public static LocalGaeEnvironment setUp() {
        LocalGaeEnvironment localEnvironment = new LocalGaeEnvironment();
        localEnvironment.gaeHelper.setUp();
        return localEnvironment;
    }

    @Override
    public void close() throws IOException {
        this.gaeHelper.tearDown();
    }

}
