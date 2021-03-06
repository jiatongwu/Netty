# Netty in Action


## Main component

### Channel
* (인바운드) 데이터와 나가는(아웃바운드) 데이터를 위한 운송수단 개념
* 하나 이상의 입출력 작업(예: 읽기 또는 쓰기)을 수행할 수 있는 하드웨어 장치 파일, 네트워크 소켓, 프로그램 컴포넌트와 같은 엔티티에 대한 열린 연결
* bind(), connect(), read(), write()
* 기본 구조는 Socket 클래스이다.



### Callback
* 다른 메스드로 자신에 대한 참조를 제공할 수 있는 메서드다.


### Future
* Future는 작업이 완료되면 이를 애플리케이션에 알리는 한 방법이다.
* 이 객체는 비동기 작업의 결과를 담는 자리표시자(placeholder) 역할을 하며, 미래의 어떤 시점에 작업이
완료 되면 그 결과에 접근할 수 있게 해준다.
* JDK의 Future 인터페이스를 제공하지만, 제공되는 구현에는 수동으로 작업완료 여부를 확인하거나 완료되기 전까지는 블로킹하는 기능만 있다.
* 네티의 모든 아웃바둔드 입출력 작업은 ChannelFuture를 반환하며 진행을 블로킹하는 작업은 없다.
* 네티는 기본적으로 비동기식이며, 이벤트 기반이다.


### Event, Handler
> 네티는 작업의 상태 변화를 알리기 위해 고유한 이벤트를 이용하며, 발생한 이벤트를 기준으로 적절한 동작을 트리거 할 수 있다.

* 로깅
* 데이터 변환
* 흐름 제어
* 애플리케이션 논리
* 연결 활성화 또는 비활성화
* 데이터 읽기
* 사용자 이벤트
* 오류 이벤트
* 원격 피어로 연결 열기 또는 닫기
* 소켓으로 데이터 쓰기 또는 플러시

![ChannelHandler](https://waylau.gitbooks.io/essential-netty-in-action/content/images/Figure%201.3%20Event%20Flow.jpg "ChannelHandler")

![Channel, EventLoop, EventLoopGroup](https://waylau.gitbooks.io/essential-netty-in-action/content/images/Figure%203.1.jpg "Channel, EventLoop, EventLoopGroup")

> 참고 하세요: [netty 관련된 키노트][nettyadvice]

[nettyadvice]: <http://hatemogi.github.io/netty-startup>
