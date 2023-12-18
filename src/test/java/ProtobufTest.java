import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
import ru.sidey.snake.SnakesProto;

public class ProtobufTest {

    @Test
    public void parseTest() throws InvalidProtocolBufferException {
        SnakesProto.GameMessage out = SnakesProto.GameMessage.newBuilder()
                .setSenderId(0)
                .setPing(SnakesProto.GameMessage.PingMsg.newBuilder().build())
                .setMsgSeq(1)
                .setReceiverId(1)
                .build();
        SnakesProto.GameMessage in = SnakesProto.GameMessage.parseFrom(out.toByteArray());
        switch (in.getTypeCase()) {
            case ACK -> System.out.println(in.getAck());
            case JOIN -> System.out.println(in.getJoin());
            case PING -> System.out.println(in.getPing());
            case ERROR -> System.out.println(in.getError());
            case ANNOUNCEMENT -> System.out.println(in.getAnnouncement());
            case STATE -> System.out.println(in.getState());
            case DISCOVER -> System.out.println(in.getDiscover());
            case STEER -> System.out.println(in.getSteer());
            case ROLE_CHANGE -> System.out.println(in.getRoleChange());
            case TYPE_NOT_SET -> System.out.println("Type not set");
        }
    }

}
