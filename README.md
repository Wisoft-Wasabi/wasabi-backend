# Wasabi : 와이소프트 사람들의 비밀노트

프로젝트 와사비는 한밭대학교 무선 통신 소프트웨어 및 데이터베이스 연구실(이하 [WiSoft](https://www.notion.so/wisoft/Documents-Public-26212c9e5ddb485e9599f205efff15e5))의 기술 블로그를 제작하고자 시작하였습니다.

연구실을 소개하기 위한 [기존의 연구실 홈페이지](https://wisoft.io/)와 달리, 이 기술 블로그에서는 연구실 멤버들만 게시글을 작성할 수 있도록 하여 경험을 공유합니다.

<br/>

## *Document.*
- [프로젝트 노션](https://www.notion.so/Wasabi-Wiki-ab1b7bb8b2d940e9a3e0d9bfe88bfee6)
  - [회의록](https://www.notion.so/96d2465f314d4162b40b21c32c9a337f)
    - [백엔드 응답 형태 구조화](#backend-response-format)
    - [백엔드 테스트 코드 컨벤션](#backend-test-code-convention)
  - [요구사항 명세서](https://www.notion.so/56e13a376538434a81f691eff5514e96)
  - [데이터베이서 설계](https://www.notion.so/afa9eeb752ab493889a75cd351c55cdf)
  - [API 명세서](https://www.notion.so/API-e04df0baa4214d7292dc81ce3fab6a20)
  - [프로젝트를 진행하며 학습한 내용](https://www.notion.so/14e3a7ec4748469388ffe5c1586e8578)
- [Front-End Repository](https://github.com/Wisoft-Wasabi/wasabi-frontend)
- [Back-End Repository](https://github.com/Wisoft-Wasabi/wasabi-backend)

<br/>

## *System Architecture.*
![wasabi-system-architecture](https://github.com/Wisoft-Wasabi/.github/assets/95692663/e9125997-6efc-424f-bbc0-7b06b334182c)

<br/>

## *Backend Response Format*
```
{
    "code":      // code
    "message":   // message
    "data": {
                 // data
    },
    "timestamp": // timestamp
}
```

<br/>

## *Backend Test Code Convention*
#### 1. BDD 패턴을 이용 : `Mockito.when()`이 아닌 `BDDMockito.given()`를 사용한다.
```java
// we use

@Test
void test() {
    // given

    given( ~~~ );

    // when
    // then
}

/* =========== */

// we don't use
@Test
void test() {
    // given
    // when

    when( ~~~ );

    // then
}
```

<br/>


#### 2. `then`절에 사용되는 검증 메서드는 `static import` 한다.
- `Assertions.assertEquals()`를 사용하지 않고, `Assertions.assertThat()`을 활용할 것
- `SoftAssertions.assertSoftly()`는 2개 이상의 결과를 검증할 경우에만 사용할 것
```java
// we use

@Test
void test() {
    // given
    // when
    // then

    assertSoftly(softAssertions -> {
        softAssertions.assertThat( ~~~ ).isEqualTo( ~~~ );
        softAssertions.assertThat( ~~~ ).isEqualTo( ~~~ );
    });
}

/* =========== */

// we don't use
@Test
void test() {
    // given
    // when
    // then

    Assertions.assertEquals( ~~~ );
}
```

<br/>

#### 3. 테스트시 필요한 데이터들은 AutoParams를 적극 활용하고, 애너테이션 순서는 중요한 것을 테스트와 가깝게 선언한다.
- 테스트를 파악하기 쉽게 `@DisplayName()`을 가장 상단에 작성하자.
- AutoParams 관련 애너테이션은 `@ParameterizedTest`, `@AutoSource`, `@Customization` 순으로 나열한다.
```java
// we use

@DisplayName("게시글 좋아요 순 정렬 후 조회시, 좋아요가 가장 많은 게시글이 먼저 조회된다.")
@ParameterizedTest
@AutoSource
@Customization(NotSaveBoardCustomization.class)
void read_boards_order_by_likes(
    final Board board1,
    final Board board2,
    final Member member) throws Exception {

    // given
    // when
    // then  
}

/* =========== */

// we don't use
@DisplayName("게시글 좋아요 순 정렬 후 조회시, 좋아요가 가장 많은 게시글이 먼저 조회된다.")
@Test
void read_boards_order_by_likes() throws Exception {
    // given

    // 게시글 생성을 할 회원 생성 및 저장
    // 회원이 작성한 게시글 1 생성 및 저장
    // 회원이 작성한 게시글 2 생성 및 저장
  
    // when
    // then  
}
```

<br/>

#### 4. `var` 키워드 사용처
- Integration 및 Repository 테스트의 경우에 `when` 절의 실행 결과는 `var` 키워드를 사용하고, 변수명은 `result`로 통일한다.
- Controller 단위 테스트의 경우에는 `request`, `response`에도 가독성을 위해 `var` 키워드를 적용한다.
```java
// we use

@DisplayName("요청 시 정상적으로 등록되어야 한다.")
@Test
void register_like() throws Exception {

    //given
    final String accessToken = jwtTokenProvider.createAccessToken(1L, "wasabi", Role.GENERAL, false);

    final var request = new RegisterLikeRequest(1L);

    final var response = new RegisterLikeResponse(1L);
    given(likeService.registerLike(any(), any())).willReturn(response);

    //when
    final var result = mockMvc.perform(post("/likes")
        .contentType(APPLICATION_JSON)
        .header("Authorization", "Bearer" + accessToken)
        .content(objectMapper.writeValueAsString(request)));

    //then
    result.andExpect(status().isCreated());
}

/* =========== */

// we don't use
@DisplayName("요청 시 정상적으로 등록되어야 한다.")
@Test
void register_like() throws Exception {

    //given
    final String accessToken = jwtTokenProvider.createAccessToken(1L, "wasabi", Role.GENERAL, false);

    final RegisterLikeRequest request = new RegisterLikeRequest(1L);

    final RegisterLikeResponse response = new RegisterLikeResponse(1L);
    given(likeService.registerLike(any(), any())).willReturn(response);

    //when
    final ResultActions resultActions = mockMvc.perform(post("/likes")
        .contentType(APPLICATION_JSON)
        .header("Authorization", "Bearer" + accessToken)
        .content(objectMapper.writeValueAsString(request)));

    //then
    resultActions.andExpect(status().isCreated());
}
```

<br/>

## *Member.*

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/mgstyle97">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/6223be2b-43c9-4f2e-bdfd-5fbca945984b" width="90px;" alt="mgstyle97"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>김민기</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kukjun">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="kukjun"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>이국준</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Donggwon-Seo">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="Donggwon-Seo"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>서동권</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/2dongyeop">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/30bb6059-24d8-4973-b8be-8286e7abe0b6" width="90px;" alt="2dongyeop"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>이동엽</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/LimSeNa">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="LimSeNa"/><br />
        <sub><b>Frontend</b><br></sub>
        <sub><b>임세나</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Choiminseok18">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="Choiminseok18"/><br />
        <sub><b>Frontend</b><br></sub>
        <sub><b>최민석</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Jinwon-Dev">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="Jinwon-Dev"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>윤진원</b><br></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/leeseunghee00">
        <img src="http://github.com/Wisoft-Wasabi/.github/assets/95692663/375ca153-8ffc-497b-ac64-c73e6e649637" width="90px;" alt="leeseunghee00"/><br />
        <sub><b>Backend</b><br></sub>
        <sub><b>이승희</b><br></sub>
      </a>
    </td>
  </tr>
</table>
<br/>

## *Skill.*
> Front-End
- Language
  - JavaScript
- Framework
  - React

<br/>

> Back-End
- Language
  - Java 17
- Framework
  - Spring Boot 3.1.0
- Library
  - Spring Data JPA
  - Querydsl
  - Log4j2
  - [AutoParams](https://github.com/AutoParams/AutoParams)
  - Junit5
  - Mockito
- Database
  - Docker
  - MySQL
- Infrastructure
  - AWS EC2, RDS

<br/>

> Collaboration
- Figma
- Notion
- Jira
