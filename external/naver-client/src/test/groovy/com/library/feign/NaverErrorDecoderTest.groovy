package com.library.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.exception.ApiException
import com.library.exception.ErrorType
import com.library.NaverErrorResponse
import feign.Request
import feign.Response
import org.springframework.http.HttpStatus
import spock.lang.Specification

class NaverErrorDecoderTest extends Specification {
  ObjectMapper objectMapper = Mock()
  NaverErrorDecoder errorDecoder = new NaverErrorDecoder(objectMapper)

  def "에러디코더에서 에러발생 시 ApiException 예외가 발생한다."() {
    given:
    def responseBody = Mock(Response.Body)
    def inputStream = new ByteArrayInputStream()
    def response = Response.builder()
        .status(400)
        .request(Request.create(Request.HttpMethod.GET, "testUrl", [:], null as Request.Body, null))
        .body(responseBody)
        .build()

    //  `1 *`는 1번 호출 되었는지 확인
    // `>>`는 메서드 호출 시 반환할 값을 정의
    // (*_)는 메서드가 어떤 파라미터로 호출되더라도 상관없음을 표현
    1 * responseBody.asInputStream() >> inputStream
    1 * objectMapper.readValue(*_) >> new NaverErrorResponse("error!!", "SE03")

    when:
    errorDecoder.decode(_ as String, response)

    then:
    // 특정 예외가 발생했는지 확인할 때 사용
    ApiException e = thrown()
    verifyAll {
      e.errorMessage == "error!!"
      e.httpStatus == HttpStatus.BAD_REQUEST
      e.errorType == ErrorType.EXTERNAL_API_ERROR
    }

  }

}
