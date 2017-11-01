package pro.tremblay.ehcachequestions.stackoverflow.q45665733;

import org.ehcache.impl.internal.util.ByteBufferInputStream;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * @author Henri Tremblay
 */
    public class MySerializer implements Serializer<MyClass> {

      public MySerializer(ClassLoader unused) {
      }

      @Override
      public ByteBuffer serialize(MyClass object) throws SerializerException {
        try(ByteArrayOutputStream bout = new ByteArrayOutputStream(); ObjectOutputStream oout = new ObjectOutputStream(bout)) {
          oout.writeUTF(object.getValue());
          oout.flush();
          return ByteBuffer.wrap(bout.toByteArray());
        } catch (IOException e) {
          throw new SerializerException(e);
        }
      }

      @Override
      public MyClass read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        try(ObjectInputStream oin = new ObjectInputStream(new ByteBufferInputStream(binary))) {
            return new MyClass(oin.readUTF());
        } catch (IOException e) {
          throw new SerializerException(e);
        }
      }

      @Override
      public boolean equals(MyClass object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return object.equals(read(binary));
      }

    }
