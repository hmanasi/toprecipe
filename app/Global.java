import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

import com.toprecipe.configs.AppConfig;
import com.toprecipe.configs.DataConfig;

import play.GlobalSettings;
import play.Application;
import play.Logger;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class, DataConfig.class);
        //new AnnotationConfigApplicationContext("com.toprecipe.controllers","com.toprecipe.services");
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}