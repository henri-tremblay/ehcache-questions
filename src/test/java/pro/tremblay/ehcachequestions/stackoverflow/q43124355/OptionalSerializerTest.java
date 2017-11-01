package pro.tremblay.ehcachequestions.stackoverflow.q43124355;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Henri Tremblay
 */
public class OptionalSerializerTest {

  private OptionalSerializer<String> serializer = new OptionalSerializer<>(getClass().getClassLoader());

  @Test
  public void testNotEmpty() throws Exception {
    ByteBuffer buffer = serializer.serialize(Optional.of("allo"));
    Optional<String> s = serializer.read(buffer);
    assertThat(s.get()).isEqualTo("allo");
  }

  @Test
  public void testEmpty() throws Exception {
    ByteBuffer buffer = serializer.serialize(Optional.empty());
    Optional<String> s = serializer.read(buffer);
    assertThat(s.isPresent()).isFalse();
  }

}
