package f4.eventproducerservice.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
  @Bean
  public DateTimeFormatter fomatter(){
    return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  }
}
