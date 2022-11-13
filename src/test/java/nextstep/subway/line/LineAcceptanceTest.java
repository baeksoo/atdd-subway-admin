package nextstep.subway.line;

import static nextstep.subway.line.LineAcceptanceFixture.노선_결과에서_노선_색깔을_조회한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선_결과에서_노선_아이디를_조회한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선_결과에서_노선_이름을_조회한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선_목록을_조회한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선_수정_요청;
import static nextstep.subway.line.LineAcceptanceFixture.노선_요청;
import static nextstep.subway.line.LineAcceptanceFixture.노선을_삭제한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선을_생성한다;
import static nextstep.subway.line.LineAcceptanceFixture.노선을_수정한다;
import static nextstep.subway.line.LineAcceptanceFixture.특정_노선을_조회한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면 Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     **/
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> 노선_생성결과 = 노선을_생성한다(노선_요청("1호선", "Blue", "서울역", "두정역"));

        // then
        List<String> 노선_목록 = 노선_목록을_조회한다();
        String 노선명 = 노선_결과에서_노선_이름을_조회한다(노선_생성결과);
        노선_목록_중_해당_노선을_찾을_수_있다(노선_목록, 노선명);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void createLineAndFind() {
        // given
        ExtractableResponse<Response> 노선_생성_결과_2 = 노선을_생성한다(노선_요청("2호선", "Green", "까치산역", "신도림역"));
        ExtractableResponse<Response> 노선_생성_결과_3 = 노선을_생성한다(노선_요청("3호선", "Orange", "대화역", "오금역"));

        // when
        List<String> 노선_목록 = 노선_목록을_조회한다();

        // then
        String 노선명2 = 노선_결과에서_노선_이름을_조회한다(노선_생성_결과_2);
        String 노선명3 = 노선_결과에서_노선_이름을_조회한다(노선_생성_결과_3);
        노선_목록_중_해당_노선을_찾을_수_있다(노선_목록, 노선명2);
        노선_목록_중_해당_노선을_찾을_수_있다(노선_목록, 노선명3);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void createLineAndVerifyLine() {
        // given
        ExtractableResponse<Response> 노선_생성_결과_4 = 노선을_생성한다(노선_요청("4호선", "Sky", "당고개역", "남태령역"));
        Long 노선_생성_아이디 = 노선_결과에서_노선_아이디를_조회한다(노선_생성_결과_4);
        String 노선_생성_이름 = 노선_결과에서_노선_이름을_조회한다(노선_생성_결과_4);

        // when
        ExtractableResponse<Response> 특정_노선_결과 = 특정_노선을_조회한다(노선_생성_아이디);

        // then
        String 노선_이름 = 노선_결과에서_노선_이름을_조회한다(특정_노선_결과);
        Long 노선_아이디 = 노선_결과에서_노선_아이디를_조회한다(특정_노선_결과);
        두_노선_결과는_동일해야_한다(노선_생성_이름, 노선_이름);
        두_노선_결과는_동일해야_한다(노선_생성_아이디, 노선_아이디);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLineAndVerifyLine() {
        // given
        ExtractableResponse<Response> 노선_생성_결과_5 = 노선을_생성한다(노선_요청("5호선", "Purple", "방화역", "상일동역"));
        Long 노선_생성_아이디 = 노선_결과에서_노선_아이디를_조회한다(노선_생성_결과_5);

        // when
        노선을_수정한다(노선_생성_아이디, 노선_수정_요청("55호선", "Gray"));

        // then
        ExtractableResponse<Response> 특정_노선_결과 = 특정_노선을_조회한다(노선_생성_아이디);
        String 노선_이름 = 노선_결과에서_노선_이름을_조회한다(특정_노선_결과);
        String 노선_색깔 = 노선_결과에서_노선_색깔을_조회한다(특정_노선_결과);
        두_노선_결과는_동일해야_한다( "55호선", 노선_이름);
        두_노선_결과는_동일해야_한다( "Gray", 노선_색깔);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLineAndVerifyNotExists() {
        // given
        ExtractableResponse<Response> 노선_생성_결과_6 = 노선을_생성한다(노선_요청("6호선", "Ocher", "응암역", "신내역"));
        Long 노선_아이디 = 노선_결과에서_노선_아이디를_조회한다(노선_생성_결과_6);

        // when
        노선을_삭제한다(노선_아이디);

        // then
        List<String> 노선_목록 = 노선_목록을_조회한다();
        노선_목록_중_해당_노선을_찾을_수_없다(노선_목록, "6호선");
    }

    private void 두_노선_결과는_동일해야_한다(Object 노선_생성_이름, Object 노선_이름) {
        assertThat(노선_이름).isEqualTo(노선_생성_이름);
    }

    private void 노선_목록_중_해당_노선을_찾을_수_있다(List<String> 노선_목록, String 노선명) {
        assertThat(노선_목록).contains(노선명);
    }

    private void 노선_목록_중_해당_노선을_찾을_수_없다(List<String> 노선_목록, String 노선명) {
        assertThat(노선_목록).doesNotContain(노선명);
    }
}
