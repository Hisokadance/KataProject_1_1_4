import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pojo.Cat;

@Configuration
public class AppConfig {
 
    @Bean(name="helloworld")
    public HelloWorld getHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setMessage("Hello World!");
        return helloWorld;
    }

    @Scope("prototype")
    @Bean(name = "cat")
    public Cat getCat() {
        Cat cat = new Cat();
        cat.setName("meow");
        return cat;
    }
}