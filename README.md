
# 실행방법
1) git clone 
2) 도커로 로컬 디비 기동
3) src/main/resource/schema/schema.sql 에 있는 쿼리 실행해서 테이블 생성.
4) 환경변수 : test로 설정
5) src/main/java/danal/batch/restaurant/RestaurantSpringBatchApplication 스타트
6) application-test.yml  input csv 파일명 물리경로 변경필요

 ```yml
danal:
  batch:  
    chunk-size: 1000
      input:
          csv-file: C:\Users\차승연\Downloads\07_24_04_P_CSV\fulldata_07_24_04_P_일반음식점.csv  #  csv-file: data/csv/restaurant_temp_data.csv # 동적인 파일명을 받기 위해 주석처리. 잡 파라미터로 처리
```

![스타터 확인](./img/intellij%20실행.png)


# Mysql local 기동
```docker
docker pull mysql:latest

docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=1234 -v 사용자로컬경로:/var/lib/mysql -p 3306:3306 -d mysql:latest
ex) docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=1234 -v C:\Users\차승연\Desktop\docker\mysql-data:/var/lib/mysql -p 3306:3306 -d mysql:latest

docker exec -it mysql-container mysql -u root -p
```

# 테스트 결과 
[테스트 결과를 보려면 클릭](./TEST.md)