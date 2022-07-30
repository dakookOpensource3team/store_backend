# 도메인 주도 개발 시작하기
## 목차
1. 도메인 모델 시작하기
2. 아키텍처 개요
3. 애그리거트
4. 레포지토리와 모델 구현
5. 스프링 데이터 JPA를 이용한 조회 기능
6. 응용서비스와 표현 영역
7. 도메인 서비스
8. 애그리거트 트랜잭션 관리
9. 도메인 모델과 바운디드 컨텍스트
10. 이벤트
11. CQRS

### 1. 도메인 모델 시작하기

#### 도메인이란?

- 도메인을 설명하자면 예를 들어, 온라인 서점을 소프트웨어로 구현할 때, 상품 조회, 구매, 결제, 배송 추적, 주문 등의 기능을 제공해야 한다. 여기서 소프트웨어로 해결해야할 문제영역을 도메인이라고 한다.
- 온라인 서점이라는 도메인 은 주문, 회원, 배송, 결제, 카탈로그라는 하위 도메인으로 구분된다. 모든 하위 도메인을 위한 소프트웨어 기능을 구현을 안해도 된다. 
  - 예를들면 yes24같은 상점에서 책을 판매까지만 하지 배송은 직접 하지 않는다. 그저 배송 추적에 대한 기능을 구현한다. 이렇게 단순하게 추적하는 기능만 넣어도 문제 없다.



#### 도메인 전문가와 개발자간 지식 공유

- 각 도메인(즉 문제영역)마다 해당 비즈니스에 대한 전문가가 존재한다. 이들은 그들의 지식과, 경험을 통해 본인들이 원하는 소프트웨어 기능에 대하여 요구한다.
- 개발자는 도메인 전문가의 요구사항을 분석하고, 설계하여 소프트웨어에 기능을 구현한다. 이과정에서 요구사항을 분석하는 첫 단추가 매우 중요하다. <br>만약 도메인 전문가와 소통이 원할하지 않아, 그들이 원하는 기능이 아닌 이상한 기능이 구현될 수 있다. 그리고 기능이 구현됬을 때 다시 고치려 하려면 `다시 원래대로 돌아가는 시간` + `요구사항에 맞는 기능을 구현하는 시간` 을 허비해야 할 것이다. <br>개발자가 아닌 비즈니스를 진행하는 사람들에게 시간은 금이다. 그러니 처음부터 그들과 원할한 소통을 진행하여, 첫 단추를 잘 못 꿰는일이 없도록 해야한다.



#### 도메인 모델

**도메인 모델이란?**

- 난 여태까지 도메인과 도메인 모델은 동일하게 생각했다. 엄연히 보면 도메인이라는 범위 안에 도메인 모델이 속해있는 것이다.
- 도메인 모델은 특정 도메인을 개념적으로 표현한 것이다.
  - 주문 도메인을 떠올려보자, 주문 도메인은 주문 번호, 총 주문 금액등을 속성으로 가지고, 배송지 입력, 주문 취소같은 기능을 가진다.
- 도메인 모델의 종류
  - 객체 도메인 모델
  - 상태 다이어그램 등

- 도메인 모델은 도메인 전문가와 커뮤니케이션을 하기위해 필요하다.



도메인 모델 패턴 <-> 트랜잭션 스크립트 패턴

트랜잭션 스크립트 패턴: 도메인은 그냥 단순하게 속성값만 저장하고, 모든 로직을 서비스에 구현한다.



#### 도메인 모델 패턴

일반적인 ddd 아키텍쳐의 패턴은

~~~
			표현
-----------------
			응용
-----------------
		 도메인
-----------------
   인프라스트럭쳐
~~~

위와 같이 나뉘어 진다.

**아키텍처 구성**

| 영역                                | 설명                                                         |
| ----------------------------------- | ------------------------------------------------------------ |
| 사용자 인터페이스 - UI, Presentaion | 사용자의 요청을 처리하고 사용자에게 정보를 보여준다. 여기서는 사용자가 단순 사람뿐 아니라 웹프론트엔드, 안드로이드같은 클라이언트 애플리케이션이 될 수 있다(API로 응답 제공). |
| 응용  - Application                 | 사용자가 요청한 기능을 실행한다. 업무로직을 직접 구현하지 않으며 도메인 계층을 조합해서 기능을 실행한다. |
| 도메인 - Domain                     | 시스템이 제공할 도메인 규칙을 구현한다.                      |
| 인프라 스트럭쳐 - Infrastructure    | 데이터베이스나 메시징 시스템과 같은 외부 시스템과의 연동을 처리한다. |

**도메인 계층**

- 도메인 계층은 도메인의 핵식 규칙을 구현한다. 주문 도메인의 경우 "출고 전에 배송지를 변경할 수 있다"라는 규칙과, "주문 취소는 출고 전에 할 수 있다."라는 규칙을 구현한 코드가 도메인 계층에 위치하게 된다.
- 핵심 규칙을 구현한 코드는 도메인 모델에만 위치하기 때문에 규칙이 바뀌거나 규칙을 확정해야 할 때, 다른 코드에 영향을 덜 주고 변경 내역을 모델에 반영할 수 있게된다.
  - 응용 영역은 도메인의 메소드만 조합하여 구현한다.
- hashCode를 구현하는 이유?
  - Hash를 이용하는 자료구조 예를 들어 HashSet, HashMap과 같은 자료구조에서 key를 해시를 이용하여 저장하기때문에 hashCode를 구현해야 한다. -> 이펙티브 자바 책을 읽으면 알 수 있음



#### 유비 쿼터스 언어

개발자는 도메인과 코드사이에서 불필요한 해석을 줄이기 위해 유비쿼터스 언어를 쓰면 좋다.

예를 들면 주문 상태에 대한 enum을 구현할 때 STEP1, STEP2, STEP3로 구현하는 것이 아니라 PAYMENT_WATING, PREPARING, SHIPPED와 같이 바로 해석이 가능할 수 있게 코딩을 한다.



### 2. 아키텍처 개요

#### 4 개의 영역

'표현', '응용', '도메인', '인프라스트럭처'는 아키텍처를 설계할 때 출현하는 전형적인 4가지 영역이다.

- 4영역 중 표현 영역은 사용자의 요청을 받아 응용 영역에 전달하고 응용 영역의 처리 결과를 다시 사용자에게 보여주는 역할을 한다.
- **표현 영역**
  - Spring MVC가 표현 영역을 위한 기술에 해당된다.
  - 웹 애플리케이션에서 표현 영역의 사용자는 웹 브라우저를 사용하는 사람일 수 도 있고, REST API를 호출하는 외부 시스템일 수 있다.
  - ![](./img/presentation_section.jpeg)

- **응용 영역**

  - 표현 영역을 통해 사용자의 요청을 전달받은 응용 영역은 시스템이 사용자에게 제공해야할 기능을 구현하는데 `'주문 등록', '주문 취소', '상품 상세 조회'`와 같은 기능 구현을 예로 들 수 있다.

  - 응용 영역은 기능을 구현하기 위해 도메인 영역의 도메인 모델을 사용한다.

    ~~~java
    @Service
    @RequiredArgsConstructor
    public class CancelOrderService {
    
      private final OrderRepository orderRepository;
    
      @Transactional
      public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        optionalOrder.ifPresent(Order::cancel);
      }
    }
    ~~~

  - 응용 서비스는 로직을 직접 수행하기보다는 도메인 모델에 로직 수행을 위임한다. <br>이와 반대되는 아키텍처는 1장에 영철님께서 설명해주신 트랜잭션 스크립트 패턴이다.(서비스 에서 모든 로직을 다 구현함.)
  - ![application_section](./img/application_section.jpeg)

- **도메인 영역**
  - 도메인 영역은 도메인 모델을 구현한다. 1장에서 봤던 Order, OrderLine, ShippingInfo와 같은 도메인 모델이 이 영역에 위치한다.
  - 도메인 모델은 도메인의 핵심 로직을 구현한다.
    - ex) 주문 도메인 - 배송지 변경, 결제 완료, 주문 취소, ...
    - ex) 라스트마일 주문 도메인 - 주문 생성, 주문 변경, 주문 취소, ...
- **인프라 스트럭쳐 영역**
  - 인프라 스트럭쳐 영역은 구현 기술에 대한 것을 다룬다.
  - 이 영역은 RDBMS 연동 처리, 메시징 큐에 메시지를 전송하거나 수신하는 기능을 구현한다. 몽고 DB나 Redis와의 데이터 연동을 처리한다.
  - 인프라 스트럭쳐 영역은 논리적인 개념을 표현하기 보다는 실제 구현을 다룬다.
  - ![infrastructure_section](./img/infrastructure_section.jpeg)



- **도메인 영역, 응용 영역, 표현 영역은 구현 기술을 사용한 코드를 직접 말드지 않는다. 대신 인프라스트럭처 영역에서 제공하는 기능을 사용해서 필요한 기능을 개발한다. **ㅇ
  - 예를 들어 응용 영역에서 DB에 보관된 데이터가 필요하면 인프라스트럭처 영역의 DB 모듈을 사용하여 데이터를 읽어온다.



#### 계층 구조 아키텍처

- 4영역을 구성할 때 많이 사용하는 아키텍처과 아래의 그림과 같은 계층 구조이다.
- 표현 영역과 응용 영역은 도메인 영역을 사용하고, 도메인 영역은 인프라스트럭처 영역을 사용하므로 계층 구조를 적용하기에 적당해 보인다.
- 도메인의 복잡도에 따라 응용과 도메인을 분리하기도 하고, 한계층으로 합치는 경우도 존재한다.
  - <img src="./img/architecture.jpeg" style="zoom:50%;" />

- 계층 구조는 특성상 상위 계층에서 하위 계층으로의 의존만 존재하고 하위 계층은 상위 계층에 의존하지 않는다.

  - 예를 들면 표현 영역은 응용영역에 의존하지만 응용영역은 반대로 표현영역에 의존하지 않거나, 응용 영역이 도메인 영역에 의존하지만 도메인 영역은 응용영역에 의존하지 않는다.

  - 계층 구조를 엄격하게 적용한다면 상위 계층은 바로 아래의 계층에만 의존을 가져가야 하지만, 구현의 편리함을 위해 약간의 유연성과 융통성을 적용할 수있다. 

    - 예를 들면 응용영역은 인프라스트럭처 영역을 의존하면 않되지만 외부시스템과의 연동을 위해 도메인보다 더 아래 계층인 인프라 스트럭처 영역을 의존하기도 한다.

  - <img src="/Users/xodn5235/Documents/Study/ddd_start/img/architecture2.jpeg" alt="architecture2" style="zoom:50%;" />

  - 하지만 이렇게 되면, 응용 계층과 도메인 계층은 인프라스트럭처 계층에 종속이 된다.

  - CalculateDiscountService에서 DroolsEngine을 통해 할인가격을 구하는 기능을 구현해 보았다.

    ~~~java
    @Service
    @RequiredArgsConstructor
    public class CalculateDiscountService {
    
      private final DroolsRuleEngine droolsRuleEngine;
      private final CustomerRepository customerRepository;
    
      public void calculateDiscount(List<OrderLine> orderLines, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer customer = optionalCustomer.orElseThrow(NoSuchElementException::new);
    
        List<?> facts = Arrays.asList(customer, new Money());
        droolsRuleEngine.evalutate("discountCalculate", facts);
      }
    }
    
    =====================================================================================
    
    @Slf4j
    @Component
    public class DroolsRuleEngine {
    
      public void evalutate(String sessionName, List<?> facts) {
        //간략하게 함
        log.info(sessionName + " 세션에서 할인 금액을 계산합니다.");
      }
    }
    ~~~

  - 위 코드의 문제점은 CalculateDiscountService가 Drools 자체에 의존하지 않는다고 생각할 수 있지만, "discountCalculate"는 Drools의 세션을 의미한다. 만약 DroolsRuleEngine의 세션이 변경되면 CalculateDiscountSerivce의 코드 변경이 불가피 할 것이다.
  - 이렇게 인프라스트럭처에 의존하면 '테스트 어려움'과 '기능 확장의 어려움'이라는 두 가지 문제가 발생하는 것을 알 수 있다. 이러한 문제를 하기 위해서는 DIP를 이용하면 된다.



#### DIP (Dependency Inversion Principle) 의존성 역전 원칙

- 가격할인 계산을 하려면 아래의 그림과 같이 고객 정보를 구하고, 구한 고객의 정보와 주문 정보를 이용해서 룰을 실행해야 한다.

<img src="./img/high_low_module.jpeg" alt="high_low_module" style="zoom:30%;" />

- 여기서 CalculateDiscountService는 고수준 모듈이다. 고수준 모듈은 의미 있는 단일 기능을 제공하는 모듈로 CalculateDiscountService는 가격 할인 계산이라는 기능을 구현한다.
  - 고수준 모듈을 기능을 구현하려면 여러 하위 기능이 필요하다. 가격 할인 계산 기능을 구현하려면 고객 정보를 구해야 하고 룰을 실행해야 하는데 이 두기능이 하위 기능이다. (고위 기능: 가격 할인 계산, 하위 기능: 고객 정보 구하기, 할인 룰 실행)
  - 저수준 모듈은 위에 서술해놓은 하위 기능을 실제로 구현한 것이다. 그림과 같이 JPA를 이용해서 고객 정보를 읽어오는 모듈과 Drools로 룰을 실행하는 모듈이 저수준 모듈이 된다.
- 고수준 모듈이 제대로 동작하려면 저수준 모델을 사용해야 한다. 그런데 고수준 모듈이 저수준 모듈을 사용하면 구현 변경과 테스트가 어렵다는 문제에 직면한다.
- DIP는 이문제를 해결하기 위해 저수준 모듈이 고수준 모듈에 의존하도록 바꾼다.

~~~java
public class CalculateDiscountService {

  private final CalculateRuleEngine calculateRuleEngine;
  private final CustomerRepository customerRepository;

  public void calculateDiscount(List<OrderLine> orderLines, Long customerId) {
    Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
    Customer customer = optionalCustomer.orElseThrow(NoSuchElementException::new);

    List<?> facts = Arrays.asList(customer, new Money());
    calculateRuleEngine.evalutate(facts);
  }
}

=====================================================================================

public interface CalculateRuleEngine {

  public void evalutate( List<?> facts);
}

=====================================================================================

@Slf4j
@Component
public class DroolsRuleEngine implements CalculateRuleEngine{

  public void evalutate(List<?> facts) {
    final String session = "droolsRuleSession";
    log.info(session + "를 이용하여 할인 금액을 계산합니다.");
  }
}
~~~

- CalculateDiscountService에서 DroolsEngine에 대해 의존하는 코드가 사라졌다. DroolsEngine을 추상화한 CalculateRuleEngine를 의존하고 있다. 실제로는 런타임때 DroolsEngine이 실행된다.

- 의존 구조가 아래의 그림과 같이 변경 되었다.

  <img src="./img/high_low_module2.jpeg" alt="high_low_module2" style="zoom:33%;" />

- 해당 구조를 보면 CalculateDiscountService는 더이상 구현 기술인 Drools에 의존하지 않고 추상화한 CalculateRuleEngine을 의존한다.
  - 룰을 이용한 할인 금액 계산은 고수준 모듈의 개념이므로 CalculateRuleEngine 인터페이스는 고수준 모듈에 속한다.
  - DroolsRuleEngine은 고수준 모듈인 CalcualteRuleEngine을 구현한 것이므로 저수준 모듈에 속한다.(저는 책과 다르게 이해하였습니다.)
    - 책에는 "DroolsRuleEngine은 고수준의 하위 기능인 CalcualteRuleEngine를 구현한 것이므로 저수준 모듈에 속한다." 이렇게 적혀있습니다.

