<br>

## 동네잡일

<br>

### 소소한 일상 얘기나 일손이 필요한 경우 게시글을 작성하여 내 주변 사람들에게 공유할 수 있는 서비스 입니다.

## 목차

* [앱 소개](#앱-소개) 
* [동네잡일 주요 기술 스택](#동네잡일-주요-기술-스택)
* [처음 적용해본 경험들](#처음-적용해본-경험들)
* [현재 코드의 문제점](#현재-코드의-문제점)
* [TODO](#TODO)



<br>
<div>
<img width="18%" src="https://user-images.githubusercontent.com/31702431/143905196-74a8e3a7-2036-4a7c-9d58-dd8e6112a217.png">
<img width="18%" src="https://user-images.githubusercontent.com/31702431/143907558-8e85197a-d5cc-457b-bf14-a89fc7d0b8f9.png">
<img width="18%" src="https://user-images.githubusercontent.com/31702431/143905219-351d8e5a-e64a-4af5-a1e2-251d726d3af9.png">
<img width="18%" src="https://user-images.githubusercontent.com/31702431/143905230-6c0c68f3-39b2-4059-9fd1-8df85b1d98e4.png">
<img width="18%" src="https://user-images.githubusercontent.com/31702431/143905247-c614d496-f9ed-4188-ab17-52b7cd624599.png">
</div>
<br>

[구글플레이 링크]
- 기간 : 2021.10 ~ 2021.12
- 팀 구성 : android 1명, backend 1명
- 역할 : android 앱 개발
- 내용 : 소소한 일상 얘기나 일손이 필요한 경우 게시글을 작성하여 내 주변 사람들에게 공유할 수 있는 서비스 입니다.

<br>
<br>

## 동네잡일 주요 기술 스택
- Kotlin
- Firebase FCM
- MVVM
- Hilt
- AAC
- Retrofit2
- Clean Architecture
- Room
- SharedPreferences
- DataBinding

<br>
<br>

## 처음 적용해본 경험들
- token 헤더에 담아 전달
  - 이전 회사에서는 항상 파라미터로 필수정보를 넣느라 매번 귀찮음을 느낌
  - 현재 회사에서 하는 방식을 보고 신세계를 경험해 좋은 지식을 습득해 프로젝트에 적용함(신세계...)
- Login Session 처리 기능
  - 로그인 세션을 처음 접해봄.
  - 로그인 api호출 할 떄 쿠키에 전달하여 오는 Session을 SharedPreferences에 저장을 시켜놓고, api호출을 할 때 현재 로그인 되어있는 Session을 보내줘 하나의 Session을 가지고 처리.
- 비슷한 레이아웃 뷰모델 처리
  - 과거의 나 : 액티비디 하나당 뷰모델 하나를 가지고 있었는데, 비슷한 레이아웃의 뷰를 가질 때 viewmodel을 전달받는데 해당 뷰모델의 클래스가 달라 같은 뷰를 여러개 생성...
  - 개선 : 비슷한 뷰모델의 부모 클래스를 생성해 상속받아서 사용...(이렇게 간단한것을...😭)
- 네트워크 및 에러 처리
  - 과거의 나 : api호출할 때마다 네트워크에러/서버에러/타임아웃에러 등을 체크함으로써 보일러 플레이트코드가 생김
  - 개선 : livedata로 만들고, 나만의 에러 규칙을 통해 공통으로 사용할 수 있도록 부모 Activity와 부모 viewModel을 만들어서 처리. (매번 에러처리를 하지 않아도 알아서 자동으로 에러처리가 되니 편리해짐)

<br>
<br>

## 현재 코드의 문제점
- DataBinding으로 RecyclerView의 adapter를 xml에서 받아오고 있다.
  - 의문점 : adapter가 실시간으로 변경되는 것도 아닌데 단지 kotlin코드를 줄이자고 DataBinding을 사용해야할까? 라는 생각이 들었다.
  - 개선사항 : adapter를 binding하지 않고, 그 안에 있는 data를 바인딩 하는 방식으로 변경 
- Adapter에 ViewModel을 같이 주입해서 보내줘야할까?
  - 현재 개선사항 생각중...

코드 개편 후 링크 투척

<br>
<br>

## Git 버전관리

## ✅ TODO

- [x] CI/CD 툴 연동 (2022. 01 전까지 결정하기)
    - [x] 비트라이즈 or 젠킨스 고민중 
- [x] 테스트 코드 작성 (2022. 02 공부 후 적용)
- [x] 코드 리펙토링 (2022.01 수정하기)
- [x] 좋아요 기능 (위 작업 완료 후 기능 추가)
- [x] 마이페이지 개편 
- [x] 팔로우
    - [x] 사용자 팔로우
    - [x] 사용자 언팔로우
    - [x] 사용자의 팔로워 리스트 조회
    - [x] 사용자의 팔로잉 리스트 조회
