package proxetta;

import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;
import proxetta.test.FooReplace;


public class LogProxyAdvice implements ProxyAdvice {

    public Object execute() {

        String methodName = ProxyTarget.targetMethodName();
        System.out.println("methodName:"+methodName);
        if (methodName.equals("testMethod")){
            new FooReplace().otherTestMethod();
            return null;
        }else{
            return ProxyTarget.invoke();
        }
    }

}
