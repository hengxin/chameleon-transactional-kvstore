package messaging;

/**
 * @author hengxin
 * @date 16-9-1
 */
public interface IMessageListener2 {
    void accept();
    void register(IMessageConsumer consumer);
}
