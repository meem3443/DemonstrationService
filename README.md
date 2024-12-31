# DemonstrationSevice

<br>

![app_icon](https://github.com/JungWooGeon/DemonstrationService/assets/61993128/3c843212-d95f-481c-a1b4-11b08247054b)

<br><br>

### 💡 시위 관리 서비스 <br>
Android 시위가 발생하였을 때, 측정된 소음을 통해 명령서 등을 발송하며 시위를 관리하는 앱
<br><br>

### 🛠 사용
 - android, android studio
 - java
 - Room DB, Retrofit2, Glide, Rxjava, Goolgle Maps API

<br><br>

### ⭐️ 기능
 - 대상 소음도 산출
   - GPS 를 이용하여 현재 좌표 조회
   - 공공데이터 포털 (한국천문연구원_출몰시각 정보) API 를 통해 일출, 일몰 시각 조회
   - 배경 소음도와 측정값을 입력하고, 주어진 보정식에 여러 값들을 대입하여 대상 소음도 산출
 - 메시지 저장
   - 추후에 안내문 혹은 명령서와 같이 보낼 메시지를 저장
 - 명령서 발송
   - Google Maps API 를 사용하여 시위 위치와 측정 위치 설정
   - 시간대, 대상 지역, 소음도 등 여러 정보 입력 후 명령서 만들기 (서버에서 제작)
 - 기타
   - 이미지 확대 / 축소 (대상 소음도 기준표, 명령서)
   - 명령서 발부 리스트
     - 명령서 발송 기록 자동 저장
     - 명령서 삭제
   - 명령서 이미지 저장
   - 명령서 전송 (명령서 이미지와 메시지를 Android 기본 '메시지' 앱으로 전달)

<br><br>

### 📷 화면

<img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/f4c17859-cb85-46a3-b875-364e53b56888" width="315" height="560"/> <img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/4f2d5756-6107-41d8-bd49-16bf0d78efa5" width="315" height="560" />

<img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/93bc9948-ccd0-482e-8be0-d1078acecbd6" width="315" height="560"/> <img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/154798a7-d715-4cc8-82a8-9836f311e03e" width="315" height="560" />

<img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/cc3c20f2-0a91-4834-affa-2db2a1bca9c3" width="315" height="560"/> <img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/9906310c-8a5d-4023-9f90-9bdbaba92279" width="315" height="560" />

<img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/a53bf704-6bd5-4674-ae4d-2f152bf454d0" width="315" height="560"/> <img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/10c3ac13-8992-4941-925f-31f4f1ef9481" width="315" height="560" />

<img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/1ac8ae2f-634c-4a19-9183-6f334e494401" width="315" height="560"/> <img src="https://github.com/JungWooGeon/DemonstrationService/assets/61993128/5bcc8830-5071-4a66-af6a-d2b521777e00" width="315" height="560" />

<br>

