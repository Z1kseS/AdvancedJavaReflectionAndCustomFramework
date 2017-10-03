package application.classes;

public class LowerCasingInterceptor implements Interceptor {

    @Override
    public String interceptOutputString(String interceptedString) {        
        return interceptedString.toLowerCase();
    }

}
