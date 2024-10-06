/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  apapl.env.AgentListener
 *  apapl.env.exceptions.ActException
 *  apapl.env.exceptions.AgentException
 *  apapl.env.exceptions.EnvironmentInterfaceException
 *  apapl.env.exceptions.NoEnvironmentException
 */
package apapl;

import apapl.ExternalActionFailedException;
import apapl.data.APLFunction;
import apapl.data.Term;
import apapl.env.AgentListener;
import apapl.env.exceptions.ActException;
import apapl.env.exceptions.AgentException;
import apapl.env.exceptions.EnvironmentInterfaceException;
import apapl.env.exceptions.NoEnvironmentException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment {
    protected HashMap<String, AgentListener> agents = new HashMap();

    protected void addAgent(String string) {
    }

    protected void removeAgent(String string) {
    }

    public final void throwEvent(APLFunction aPLFunction, String ... stringArray) {
        this.notifyAgents(aPLFunction, stringArray);
    }

    public final String getName() {
        String string = this.getClass().getName();
        return string.substring(0, string.lastIndexOf("."));
    }

    public final void registerAgent(String string, AgentListener agentListener) {
        this.agents.put(string, agentListener);
        this.addAgent(string);
    }

    public void deregisterAgent(String string) throws AgentException {
        if (!this.agents.containsKey(string)) {
            return;
        }
        this.agents.remove(string);
        this.removeAgent(string);
    }

    protected final void notifyAgents(APLFunction aPLFunction, String ... stringArray) {
        try {
            if (stringArray.length == 0) {
                for (String string : this.agents.keySet()) {
                    this.agents.get(string).handleMessage(string, aPLFunction);
                }
                return;
            }
            for (String string : stringArray) {
                if (!this.agents.containsKey(string)) {
                    throw new EnvironmentInterfaceException("Agent " + string + " has not registered to the environment.");
                }
                this.agents.get(string).handleMessage(string, aPLFunction);
            }
        }
        catch (EnvironmentInterfaceException environmentInterfaceException) {
            environmentInterfaceException.printStackTrace();
        }
    }

    public final Term performAction(String string, APLFunction aPLFunction) throws ActException, NoEnvironmentException {
        if (!this.agents.containsKey(string)) {
            throw new ActException("Agent \"" + string + "\" is not registered.");
        }
        ArrayList<Term> arrayList = aPLFunction.getParams();
        Class[] classArray = new Class[arrayList.size() + 1];
        classArray[0] = String.class;
        for (int i = 0; i < arrayList.size(); ++i) {
            classArray[i + 1] = arrayList.get(i).getClass();
        }
        try {
            Method method = this.getClass().getMethod(aPLFunction.getName(), classArray);
            if (!Class.forName("apapl.data.Term").isAssignableFrom(method.getReturnType())) {
                throw new ActException("Wrong return-type");
            }
            Object[] objectArray = new Object[arrayList.size() + 1];
            objectArray[0] = string;
            for (int i = 0; i < arrayList.size(); ++i) {
                objectArray[i + 1] = arrayList.get(i);
            }
            return (Term)method.invoke(this, objectArray);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ActException("Class not found", (Exception)classNotFoundException);
        }
        catch (SecurityException securityException) {
            throw new ActException("Security exception", (Exception)securityException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new ActException("No such method", (Exception)noSuchMethodException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ActException("Illegal argument", (Exception)illegalArgumentException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new ActException("Illegal access", (Exception)illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof ExternalActionFailedException) {
                throw new ActException("Execution failed.", (Exception)invocationTargetException.getCause());
            }
            if (invocationTargetException.getCause() instanceof NoEnvironmentException) {
                throw (NoEnvironmentException)invocationTargetException.getCause();
            }
            throw new ActException("Invocation target exception", (Exception)invocationTargetException);
        }
    }
}

