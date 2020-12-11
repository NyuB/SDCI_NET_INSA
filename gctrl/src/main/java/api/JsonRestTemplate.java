package api;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

public class JsonRestTemplate extends RestTemplate {
	public JsonRestTemplate() {
		super(Arrays.asList(new HttpMessageConverter<?>[] {new GsonHttpMessageConverter()}));
	}
}
