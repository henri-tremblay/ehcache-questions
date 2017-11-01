package pro.tremblay.ehcachequestions.stackoverflow.q43124355;

import org.ehcache.impl.serialization.PlainJavaSerializer;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import java.nio.ByteBuffer;
import java.util.Optional;

  public class OptionalSerializer<T> implements Serializer<Optional<T>> {

    private final PlainJavaSerializer<T> serializer;

    public OptionalSerializer(ClassLoader classLoader) {
        serializer = new PlainJavaSerializer<>(classLoader);
    }

    @Override
    public ByteBuffer serialize(Optional<T> object) throws SerializerException {
      return object.map(serializer::serialize).orElse(ByteBuffer.allocate(0));
    }

    @Override
    public Optional<T> read (ByteBuffer binary) throws ClassNotFoundException, SerializerException {
      if(binary.array().length > 0) {
        return Optional.of(serializer.read(binary));
      }
      return Optional.empty();
    }

    @Override
    public boolean equals(Optional<T> object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
      return object.equals(read(binary));
    }

  }
