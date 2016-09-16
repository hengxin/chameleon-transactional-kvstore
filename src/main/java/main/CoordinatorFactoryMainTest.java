package main;

import membership.coordinator.CoordinatorFactory;

/**
 * @author hengxin
 * @date 16-8-17
 */
public class CoordinatorFactoryMainTest {
    public static void main(String[] args) {
        String cfProperties = args[0];
        String toProperties = args[1];
        new CoordinatorFactory(cfProperties, toProperties);
    }
}
