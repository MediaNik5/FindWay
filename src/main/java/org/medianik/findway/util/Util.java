package org.medianik.findway.util;

import org.apache.log4j.Level;
import org.medianik.findway.App;
import org.medianik.findway.annotaion.Event;
import org.medianik.findway.annotaion.EventPriority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.medianik.findway.annotaion.EventPriority.MID_PROCESSING;

public class Util{

    public static void invokeIfNeeded(EventPriority priority, Object handler, Method method, Object... args) throws InvocationTargetException, IllegalAccessException{
        if(method.isAnnotationPresent(Event.Priority.class)){
            var event = method.getAnnotation(Event.Priority.class);
            if(event.value() == priority){
                App.log.log(Level.TRACE,
                        method.getName() + "() on " + handler.getClass().getSimpleName() + " was called");
                method.invoke(handler, args);
            }
        }else if(priority == MID_PROCESSING){
            App.log.log(Level.TRACE,
                    method.getName() + "() on " + handler.getClass().getSimpleName() + " was called");
            method.invoke(handler, args);
        }
    }
    public static boolean inEpsilonRange(int toCheck, int point, int delta){
        return point - delta <= toCheck && toCheck <= point + delta;
    }
    public static boolean inRange(int toCheck, int leftBorder, int rightBorder){
        return leftBorder <= toCheck && toCheck >= rightBorder;
    }

    public static int linkXToCenter(double x){
        return (int) (x - App.getInstance().width()/2);
    }

    public static int linkYToCenter(double y){
        return (int) (y - App.getInstance().height()/2);
    }
}
