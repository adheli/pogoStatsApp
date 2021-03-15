package ie.ait.tavares.pogo.external.api.pogostats;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PogoStatsModel {

    private Integer id;
    private String name;
    private Integer at;
    private Integer df;
    private Integer st;
}
