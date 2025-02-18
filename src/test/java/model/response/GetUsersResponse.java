package model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import model.user.FullInfoUser;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUsersResponse {
    private List<FullInfoUser> data;
}
