package proxetta;

import jodd.proxetta.*;
import jodd.proxetta.impl.ProxyProxetta;
import jodd.proxetta.pointcuts.ProxyPointcutSupport;
import proxetta.test.Foo;

public class TestProxetta {


    public static void main(String[] args) {

        ProxyPointcut pointcut = new ProxyPointcutSupport() {
            public boolean apply(MethodInfo methodInfo) {
                return true;
            }
        };

        ProxyAspect aspect = new ProxyAspect(LogProxyAdvice.class, pointcut);
        ProxyProxetta proxetta = ProxyProxetta.withAspects(aspect);
        Foo test = (Foo)proxetta.builder(Foo.class).newInstance();


        test.testMethod();



    }

}
