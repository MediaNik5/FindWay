package org.medianik.findway.util;

import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
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
                App.logger.log(Level.TRACE,
                        method.getName() + "() on " + handler.getClass().getSimpleName() + " was called");
                method.invoke(handler, args);
            }
        }else if(priority == MID_PROCESSING){

            App.logger.log(Level.TRACE,
                    method.getName() + "() on " + handler.getClass().getSimpleName() + " was called");
            method.invoke(handler, args);
        }
    }

    public static boolean inEpsilonRange(int toCheck, int point, int delta){
        return point - delta <= toCheck && toCheck <= point + delta;
    }

    public static boolean inRange(int toCheck, int leftBorder, int rightBorder){
        return leftBorder <= toCheck && toCheck <= rightBorder;
    }

    public static int linkXToCenter(double x){
        return (int) (x - App.getInstance().width()/2);
    }

    public static int linkYToCenter(double y){
        return (int) (y - App.getInstance().height()/2);
    }

    public static boolean isPresent(Object o){
        return o != null;
    }

    @NotNull
    public static String beautify(String startingString){
        final int tabLength = 4;
        int tabs = 0;
        StringBuilder sb = new StringBuilder(startingString.length());
        sb.append('\n');

        for(int i = 0; i < startingString.length(); i++){
            char c = startingString.charAt(i);
            if(c == '{' || c == '['){
                tabs++;
                sb.append(c);
                sb.append('\n');
                sb.append(" ".repeat(tabs*tabLength));
            }else if(c == '}' || c == ']'){
                tabs--;
                sb.append('\n');
                sb.append(" ".repeat(tabs*tabLength));
                sb.append(c);
            }else if(c == ','){
                sb.append(c);
                sb.append('\n');
                sb.append(" ".repeat(tabs*tabLength - 1));
            }else
                sb.append(c);
        }
        return sb.toString();
    }
}
