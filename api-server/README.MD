* API 호출 방법

GET /users : 사용자 정보 조회
header = email : foo@foobar.com

POST /tokens = 발급
header = userNo : 2, password : zmfltm

DELETE /tokens = 만료
header = token : 424b9ea839820b3a

GET /tokens = 검증
header = token : 424b9ea839820b3a