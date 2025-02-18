package model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import model.resource.Resource;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetResourceResponse {
    private List<Resource> data;
}
