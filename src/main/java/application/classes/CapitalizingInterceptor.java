package application.classes;

public class CapitalizingInterceptor implements Interceptor {

    @Override
    public String interceptOutputString(String interceptedString) {
        return interceptedString.toUpperCase();
    }
}
