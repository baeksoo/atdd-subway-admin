package nextstep.subway.line;

import static nextstep.subway.station.StationAcceptanceFixture.지하철_생성_결과에서_지하철역_번호를_조회한다;
import static nextstep.subway.station.StationAcceptanceFixture.지하철_역을_생성한다;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineUpdateRequest;
import org.springframework.http.MediaType;

public class LineAcceptanceFixture {

    public static LineRequest 노선_요청(String 노선명, String 노션_색깔, String 상행_지하철역, String 하행_지하철역) {
        ExtractableResponse<Response> 상행_지하철역_생성_결과 = 지하철_역을_생성한다(상행_지하철역);
        ExtractableResponse<Response> 하행_지하철역_생성_결과 = 지하철_역을_생성한다(하행_지하철역);
        Long 상행_지하철역_번호 = 지하철_생성_결과에서_지하철역_번호를_조회한다(상행_지하철역_생성_결과);
        Long 하행_지하철역_번호 = 지하철_생성_결과에서_지하철역_번호를_조회한다(하행_지하철역_생성_결과);
        return new LineRequest(노선명, 노션_색깔, 상행_지하철역_번호, 하행_지하철역_번호, 30L);
    }

    public static LineUpdateRequest 노선_수정_요청(String 노선명, String 노션_색깔) {
        return new LineUpdateRequest(노선명, 노션_색깔);
    }


    public static String 노선_결과에서_노선_이름을_조회한다(ExtractableResponse<Response> 노선_생성_결과) {
        return 노선_생성_결과.jsonPath()
                .getString("name");
    }

    public static Long 노선_결과에서_노선_아이디를_조회한다(ExtractableResponse<Response> 노선_생성_결과) {
        return 노선_생성_결과.jsonPath()
                .getLong("id");
    }

    public static String 노선_결과에서_노선_색깔을_조회한다(ExtractableResponse<Response> 노선_생성_결과) {
        return 노선_생성_결과.jsonPath()
                .getString("color");
    }

    public static List<String> 노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

    }

    public static ExtractableResponse<Response> 특정_노선을_조회한다(Long 노선_아이디) {
        return RestAssured.given().log().all()
                .pathParam("id", 노선_아이디)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_생성한다(LineRequest 노선_요청_정보) {
        return RestAssured.given().log().all()
                .body(노선_요청_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_수정한다(Long 노선_아이디, LineUpdateRequest 노선_수정_정보) {
        return RestAssured.given().log().all()
                .body(노선_수정_정보)
                .pathParam("id", 노선_아이디)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_삭제한다(Long 노선_아이디) {
        return RestAssured.given().log().all()
                .pathParam("id", 노선_아이디)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }
}
