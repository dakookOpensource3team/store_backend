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



- **도메인 영역, 응용 영역, 표현 영역은 구현 기술을 사용한 코드를 직접 말드지 않는다. 대신 인프라스트럭처 영역에서 제공하는 기능을 사용해서 필요한 기능을 개발한다. **
  - 예를 들어 응용 영역에서 DB에 보관된 데이터가 필요하면 인프라스트럭처 영역의 DB 모듈을 사용하여 데이터를 읽어온다.



#### 계층 구조 아키텍처

- 4영역을 구성할 때 많이 사용하는 아키텍처과 아래의 그림과 같은 계층 구조이다.
- 표현 영역과 응용 영역은 도메인 영역을 사용하고, 도메인 영역은 인프라스트럭처 영역을 사용하므로 계층 구조를 적용하기에 적당해 보인다.

  - 아래의 DIP에 설명이 되어 있겠지만, 아래의 그림은 런타임 의존성에 대한 구조이다.

- 도메인의 복잡도에 따라 응용과 도메인을 분리하기도 하고, 한계층으로 합치는 경우도 존재한다.
  - <img src="./img/architecture.jpeg" style="zoom:50%;" />

- 계층 구조는 특성상 상위 계층에서 하위 계층으로의 의존만 존재하고 하위 계층은 상위 계층에 의존하지 않는다.

  - 예를 들면 표현 영역은 응용영역에 의존하지만 응용영역은 반대로 표현영역에 의존하지 않거나, 응용 영역이 도메인 영역에 의존하지만 도메인 영역은 응용영역에 의존하지 않는다.

  - 계층 구조를 엄격하게 적용한다면 상위 계층은 바로 아래의 계층에만 의존을 가져가야 하지만, 구현의 편리함을 위해 약간의 유연성과 융통성을 적용할 수있다. 

    - 예를 들면 응용영역은 인프라스트럭처 영역을 의존하면 않되지만 외부시스템과의 연동을 위해 도메인보다 더 아래 계층인 인프라 스트럭처 영역을 의존하기도 한다.

  - <img src="./img/architecture2.jpeg" alt="architecture2" style="zoom:50%;" />

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
  - 테스트를 진행할 때는, Mock 객체와, Stub을 사용할 수 있다면 Stub을 좀 더 사용하는 방향으로 나아가는 것이 더 좋을 것이다.

#### 도메인 영역의 주요 구성 요소

| 요소                            | 설명                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| 엔티티<br>ENTITY                | 고유의 식별자를 갖는 객체로 자신의 라이프 사이클을 갖는다. Order, 회원, 상품과 같이 도메인의 고유한 개념을 표현한다.<br>도메인 모델의 데이터를 포함하며 해당 데이터와 관련된 기능을 함께 제공한다. |
| 밸류<br>VALUE                   | 고유의 실벼자를 갖지 않는 객체로 주로 개념적으로 하나인 값을 표현할 때 사용된다. <br>예를 들면 Address나 구매 금액을 위한 Money 와 같은 타입이 밸류 타입이다. <br>엔티티의 속성으로 사용할 뿐만아니라 다른 밸류 타입의 속성으으로도 사용된다. |
| 애그리거트AGGREGATE             | 애그리거트는 연관덴 엔티티와 밸류 객체를 개념적으로 하나로 묶은 것이다. <br>예를 들면 주문과 관련된 Order 엔티티, OrderLine 밸류, Orderer 밸류 객체를 주문 애그리거트로 묶을 수 있다. |
| 레포지토리REPOSITORY            | 도메인 모델의 영속성을 처리한다.                             |
| 도메인 서비스<br>DOMAIN SERVICE | 특정 엔티티에 속하지 않은 도멘인 로직을 제공한다.<br> 할인 금액 계산은 상품, 쿠폰, 회원 등급, 구매 금액 등 다양한 조건을 이용해서 구현하게 되는데, 이렇게 도메인 로직이 여러 엔티티와 밸류를 필요로 하면 도메인 서비스에서 로직을 구현하면 된다. |

- 책과 같이 나도 처음에는 "DB의 엔티티"와 "도메인 모델의 엔티티"가 같다고 생각했다. 책을 읽으면서 이해했을 때 "DB의 엔티티"와 "도메인 모델의 엔티티"의 차이점으로는 "도메인 모델의 엔티티"는 속성값 뿐만 아니라 도메인 기능을 함께 제공해준다는 것이다.
- 또 다른 차이점으로는 도메인 모델은 두개 이상의 개념적으로 하나인 데이터를 Value 타입으로 표현할 수 있다.
  - 배송지를 나타내는 Address, 주문자를 나타내는 Orderer 등,,

**밸류 타입**

- 밸류 타입은 불변으로 구현할 것이 권장된다. 이는 엔티티의 밸류 타입 데이터를 변경할 때는 객체 자체를 완전히 교체한다는 것을 의미한다.(REST API로 치면은 PUT?과 동일한 것 같다.)
  - 예를 들면 배송지 정보를 변경할 때 시, 구, 동 정보를 각각 변경하는 것이 아닌 한번에 Address 객체 자체로 변경한다.

**애그리거트**

- 도메인 모델이 복잡해지면 개발자가 전체 구조가 아닌 한 개 엔티티와 밸류에만 집중할 수 있는 상황이 발생한다. 이때 상위 수준에서 모델을 관리하지 않고 개별 요소에만 초점을 맞추다 보면, 큰 수준에서 모델을 이해하지 못해 큰 틀에서 모델을 관리할 수 없는 상황에 빠질 수 있다.
- 그래서 상위 수준에서 모델을 볼 수 있어야 전체 모델의 관계와 개별 모델을 이해하는데 도움이 된다. 도메인 모델에서 전체 구조를 이해하는데 도움이 되는 것이 바로 `애그리거트`이다.

**애그리거트는 관련 객체를 하나로 묶은 군집이다.**

- 애그리거트의 대표적인 예가 주문이다.
  - 주문이라는 도메인 개념은 `주문`, `배송지 정보`, `주문자`, `주문 목록`, `총 결제 금액`등의 하위 모델로 구성된다. 이 하위 개념을 표현한 모델을 하나로 묶어서 "주문"이라는 상위 개념으로 표현할 수 있다.
- 애그리 거트는 군집에 속한 객체를 관리하는 루트 엔티티를 갖는다. 루트 엔티티는 애그리거트에 속해 있는 엔티티와 벨류 객체를 이용해서 애그리거트가 구현해야 할 기능을 제공한다.

<img src="./img/aggregate.jpeg" alt="aggregate" style="zoom:25%;" />

**레포지토리**

- 도메인 객체를 지속적으로 사용하려면 RDB, NoSQL, 로컬 파일과 같은 물리적인 저장소에 도메인 객체를 보관해야 한다. 이를 위한 도메인 모델이 레포지토리이다.
- 레포지토리는 애그리거트 단위로 도메인 객체를 저장하고 조회하는 기능을 정의한다.

#### 인프라스트럭처 개요

- 인프라스트럭처는 표현 영역, 응용 영역, 도메인 영역을 지원한다. 도메인 객체의 영속성 처리, 트랜잭션, SMTP 클라이언트, REST 클라이언트 등 다른 영역에서 필요로 하는 프레임워크, 구현 기술, 보조 기능을 지원한다.
- 도메인 영역과, 응용 영역에서 인프라스트럭쳐를 직접 의존하기 보단, 상위 두영역(도메인, 응용)영역에서 정의한 인터페이스를 인프라 스트럭처 영역에서 구현하는 것이 시스템적으로 더 유연하고 테스트를 작성하기 쉬운 이점을 가져갈 수 있다.
  - 허나 구현의 편리함은 DIP가 주는 다른 장점(변경의 유연함, 테스트가 쉬움)만큼 중요하기 때문에 DIP의 장점을 해치지 않는 범위에서 응용 영역과 도메인 영역에서 구현기술에 대한 의존을 가져가는 것이 나쁘지 않다. - 책의 필자

#### 모듈 구성

- 아키텍처의 각 영역은 별도 패키지에 위치한다. 패키지 구성 규칙에 정답이 존재하는 것은 아니지만 아래의 그림들과 같이 책의 필자는 예시를 정리하였다.
  - 책의 필자는 한 패키지에 10 ~ 15개정도의 타입 개수를 유지하는 것을 권장했다.
- 그 중에서 나는 2.21을 참고하여 이번 프로젝트를 구현해보았다.

<img src="./img/module1.jpeg" alt="module1" style="zoom:30%;" />

<img src="./img/module2.jpeg" alt="module2" style="zoom:30%;" />

<img src="./img/module3.jpeg" alt="module3" style="zoom:30%;" />

// 고려 사항: 카탈로그에도 Product가 포함되고 주문에도 Product가 포함되는데, 어떻게 가져가야 할지?

### 3. 애그리거트

#### 3.1 애그리거트

![](./img/20A248EB-0E83-4576-BB8E-3ECE2B83854C.jpeg)

상단의 그림과 같이 상위 수준에서 모델을 정리하면 도메인 모델의 복잡한 관계를 이해하는데 도움이 된다.

- 백개 이상의 테이블을 한 장의 ERD에 모두 표시하면 개별 테이블간의 관계를 파악하느라 큰 틀에서 데이터 구조를 이해하는데 어려움을 겪게 되는 것처럼, 도메인 객체 모델이 복잡해지면 개별 구성요소 위주로 모델을 이해하게 되고 전반적인 구조나 큰 수준에서 도메인 간의 관계를 파악하기 어려워진다.
  - 상위 수준에서 모델이 어떻게 엮여 있는지 알아야 전체 모델을 망가뜨리지 않으면서 추가 요구사항을 모델에 반영할 수 있는데, 세부적인 모델만 이해한 상태로는 코드를 수정하는 것이 꺼려워 진다.

- 복잡한 도메인을 이해하고 관리하기 쉬운 단위로 만들려면 상위 수준에서 모델을 조망할 수 있는 방법이 필요한데, 그 방법이 

  애그리거트

  다.

  - `애그리거트는 관련된 객체를 하나의 군으로 묶어 준다. 수많은 객체를 애그리거트로 묶어서 바라보면 상위 수준에서 도메인 모델간의 관계를 파악할 수 있다. `

- 애그리거트는 모델을 이해하는 데 도움을 줄 뿐만아니라 일관성을 관리하는 기준도 된다. 모델을 보다 잘 이해할 수 있고 애그리거트 단위로 일관성을 관리하기 때문에
  - 애그리거트는 복잡한 도메인을 단순한 구조로 만들어준다. 복잡도가 낮아지는 만큼 도메인 기능을 확장하고 변경하는데 필요한 노력도 줄어든다.

- 애그리거트는 관련된 모델을 하나로 모았기 때문에 한 애그리거트에 속한 객체는 유사하거나 동일한 라이프 사이클을 갖는다.



**애그리거트의 경계**

- 애그리거트는 경계를 갖는다. 한 애그리거트에 속한 객체는 다른 애그리거트에 속하지 않는다. 애그리거트는 독립된 객체 군이며 각 애그리거트는 자기 자신을 관리할 뿐 다른 애그리거트를 관리하지 않는다.
  - 예를 들면 주문 애그리거트는 배송비를 변경하거나 주문 상품 개수를 변경하는 등 자기 자신을 관리하지만, 회원의 비밀번호를 변경하거나 상품의 가격을 변경하지는 않는다.

- 경계를 설정할 때 기본이 되는 것은 도메인 규칙과 요구사항이다. 도메인 규칙에 따라 함께 생성되는 구성요소는 한 애그리거트에 속할 가능성이 높다.

- 흔히 `A가 B를 갖는다.`로 설계할 수 있는 요구사항이 있다면 A와 B를 한 애그리거트로 묶어서 생각하기 쉽다. 주문의 경우 Order가 ShippingInfo와 Orderer를 가지므로 이는 어느정도 타당해보인다.

  - 허나 `A가 B를 갖는다.` 로 해설할 수 있는 요구사항이 있다고 하더라도 이것이 반드시 A와 B가 한 애그리거트에 속한다는 것을 의미하는 것은 아니다.

  - 예를 들면 상품과 리뷰다. 상품 상세페이지에 들어가면 상품 상세 정보와 함께 리뷰 내용을 보여줘야 한다는 요구사항이 있을 때 Product 엔티티와 Review 엔티티가 한 애그리거트에 속한다고 생각할 수 있다. 
    - 하지만 Product와 Review는 함께 생성되지 않고, 함께 변경되지도 않는다. 게다가 Product를 변경하는 주체가 상품 담당자라면 Review를 생성하고 변경하는 주체는 고객이다.

  ![](./img/5DF670EC-A58C-4C04-9673-E1B6EC0DCFCF.jpeg)

  - 처음 도메인 모델을 만들기 시작하면 큰 애그리거트로 보이는 것들이 많지만, 도메인에 대한 경험이 생기고 도메인 규칙을 제대로 이해할수록 애그리거트의 실제 크기는 줄어든다.

  

#### 3.2 애그리거트 루트

- 애그리거트는 여러객체로 구성되기 때문에 한 객체만 상태가 정상이면 안 된다. 도메인 규칙을 지키려면 애그리거트에 속한 모든 객체가 정상 상태를 가져야 한다. 주문 애그리거트에서는 OrderLine을 변경하면 Order의 totalAmounts도 다시 계산해서 총 금액이 맞아야 한다.

- 애그리거트에 속한 모든 객체가 일관된 상태를 유지하려면, 애그리거트 전체를 관리할 주체가 필요한데, 이 책임을 지는 것이 바로 애그리거트의 루트 엔티티이다.
  - 애그리거트 루트 엔티티는 애그리거트의 대표 엔티티다. 애그리거트에 속한 객체는 애기럭트 루트 엔티티에 직접 또는 간접적으로 속하게 된다.



#### 3.2.1 도메인 규칙과 일관성

- 애그리거트 루트가 단순히 애그리거트에 속한 객체를 포함하는 것으로 끝나는 것은 아니다. 에그리거트 루트의 핵심 역할은 애그리거트의 일관성이 깨지지 않도록 하는 것이다.

- 이를 위해 애그리거트 루트는 애그리거트가 제공해야 할 도메인 기능을 구현한다.
  - 예를 들면 주문 애기르거트는 배송지 변경, 상품 변경과 같은 기능을 제공하고, 애그리거트 루트인 Order에서 이 기능을 구현한다.

- 애그리거트 루트가 제공하는 메서드는 도메인 규칙에 따라 애그리거트에 속한 객체의 일관성이 깨지지 않도록 구현해야 한다.

- 도메인 규칙과 일관성을 지키기 위해 두가지를 습관적으로 사용해야 한다.
  - 단순히 필드를 변경하는 set 메서드를 공개(public)범위로 만들지 않는다.
    - 예를 들면 배송지 정보를 변경할 때, `public void setShippingInfo()`가 아닌 `public void changeShippingInfo()`로 구현해야한다.
  - 밸류 타입은 불변으로 구현한다.



#### 3.2.2 애그리거트 루트의 기능 구현

- 애그리거트 루트는 애그리거트 내부의 다른 객체를 조합해서 기능을 완성한다.
  - 쉽게 설명하자면 응용 서비스에서 여러개의 애그리거트를 조합해서 기능을 구현하는 것처럼, 애그리거트 루트도 애그리거트의 내부의 엔티티를 다른 객체를 조합해서 기능을 구현한다.



#### 3.2.3 트랜잭션 범위

- 트랜잭션 범위는 작을수록 좋다.

- 한 트랜잭션이 1개의 테이블을 수정하는 것과 3개의 태이블을 수정하는 것을 비교하면 성능에서 차이가 발생한다. 여러개의 테이블을 수정하면 잠금 대상이 많아진다는 것인데, 잠금대상이 많아지면 동시성이 떨어진다.

- 만약 두개의 애그리거트를 수정하게 된다면 하나의 애그리거트 내부에서 수정하는 것이 아니라, 두개의 애그리거트를 응용서비스에서 수정을 하도록 구현한다.

  ~~~java
  // 주문의 배송지를 변경하면서, 회원의 주소도 같이 변경하는 기능
  // 안좋은 예
  public class Order {
    Order orderer
    
    public void changeShippinInfo(ShippingInfo shippingInfo, boolean useNewShippingAddrAsMemberAddr) {
      verifyNotYetShipped();
      setShippingInfo(shippingInfo);
      if(useNewShippingAddrAsMemberAddr) {
        //다른 애그리거트 내부에서 변경하면 안됨.
        orderer.getMember().changeAddress(shippingInfo.getAddress());
      }
    }
  }
  
  // 방안 예
  public class ChangeOrderSerivce {
    @Transactional
    public void changeShippinInfo(Long orderId, ShippingInfo shippingInfo, boolean useNewShippingAddrAsMemberAddr) {
      Order order = orderRepository.findById(orderId);
      order.changeShippinInfo(shippingInfo);
      if(useNewShippingAddrAsMemberAddr) {
        Member member = memberRepository.findByOrdererId(order.getOrderer.getId)
        member.changeAddress(shippingInfo.getAddress);
      }
  }
  ~~~

- 도메인 이벤트를 사용하면 한 트랜잭션에서 한개의 애그리거트를 수정하면서도 동기나 비동기로 다른 애그리거트를 수정할 수 있다.
- 한 트랜잭션에서 한 개의 애그리거트를 변경하는 것을 권장하지만, 다음 경우에는 한 트랜잭션에서 두개 이상의 애그리거트를 변경하는 것을 고려할 수 있다.
  - 팀 표준
  - 기술 제약: 예를 들면 기술적 제약으로 이벤트 같은 기능을 도입하지 못함.
  - UI 구현의 편리



#### 3.3 리포지토리와 애그리거트

- 애그리거트는 개념적으로 하나이므로 레포지토리는 애그리거트 전체를 저장소에 영속화 해야한다.

  - 예를 들어 Order 애그리거트와 관련된 테이블이 세개라면(ex: OrderLine 등등) Order 애그리거트를 저장할 때 애그리거트 루트와 매핑되는 테이블뿐만 아니라 애그리거트에 속한 모든 구성요소에 매팅된 테이블에 데이터를 저장해야한다.

    ~~~java
    //레포지토리에서 save를 호출할 때, 루트엔티티인 order만 저장하는 것이 아닌 애그리거트 내부의 모든 엔티티를 저장해야한다.
    orderRepository.save(order);
    ~~~

- 동일하게 애그리거트를 구하는 레포지터리 메서드는 완전하 애그리거트를 제공해야 한다.
- 저장소로 사용하는 RDBMS, NOSQL에 상관 없이 애그리거트 상태가 변경되면 모든 변경을 원자적으로 저장소에 반영해야 한다.
  - 애그리거트의 두개의 객체를 변경하였는데, 그중 한가지만 적용이 된다면 데이터의 일관성이 깨지므로 문제가 된다.



#### 3.4 ID를 이용한 애그리거트 참조

- ORM을 통해 애그리거트간의 참조는 필트들 통해 쉽게 구현할 수 있다. 
  - 예를 들어 주문 애그리거트에 속해있는 Orderer는 주문의 회원을 참조하기 위해 Member를 필드로 참조할 수 있다.
  - 필드를 이용해서 다른 애그리거트를 직접 참조하는 것은 개발자에게 구현의 편리함을 제공한다.
  - 하지만 필드를 이요한 애그리거트 참조는 3가지의 문제를 야기할 수 있다.
    - 편한 탐색 옹요
    - 성능에 대한 고민 -> 예를 들어 Order를 조회할 때, 회원정보 조회는 필요없는데 필드르 참조된 모든 연관된 객체의 쿼리가 발생한다.
    - 확장 어려움
  - 애그리거트를 직접 참조할 때 발생할 수 있는 가장 큰 문제는 편리함을 오용할 수 있다는 것이다.
    - 한 애그리거트 내부에서 다른 애그리거트 객체에 접근할 수 있으면 다른 애그리거트의 상태를 쉽게 변경할 수 있게된다.
    - 한 애그리거트에서 다른 애그리거트의 상태를 변경한느 것은 애그리거트 간의 의존 결합도를 높여서 결과적으로 애그리거트의 변경을 어렵게 만든다.
- **위 문제들을 완화하기 위해 사용할 수 있는 것이 ID를 이용해서, 다른 애그리거트를 참조하는 것이다. DB테이블에서 외래키로 참조하는 것과 비슷하게 생각하면 된다.**
  - ID 참조를 사용하면 모든 객체가 참조로 연결되지 않고 한 애그리거트에 속한 객체들만 참조로 연결된다.
  - 이는 애그리거트의 경계를 명확히하고 애그리거트 간 물리적인 연결을 제거하기때문에 모델의 복잡도를 낮춰준다. -> 즉 애그리거트간의 결합도를 낮춰준다.
  - ID를 이용한 참조방식을 상요하면 복잡도를 낮추는 것과 함께 한 애그리거트에서 다른 애그리거트를 수정하는 것이 불가능해 진다.
  - 애그리거트별로 당른 구현기술을 사용하는 것도 가능해진다. ID로만 참조하기 때문에 결합이 없기 때문이다.
    -  예를 들면 주문 애그리거트는 중요한 정보이니, RDBMS에 저장하고, 조회 성능이 중요한 상품 애그리거트는 NoSQL로 저장할 수 있다.



#### 3.4.1 ID를 이요한 참조와 조회 성능

- 다른 애그리거트를 ID로 참조하면 참조하는 여러 애그리거트를 읽을 때 조회 속도가 문제될 수 있다.

  - 예를 들면 주문 목록을 보여주려면 상품 애그리거트와 회원 애그리거트를 함께 읽어와야 하는데, 이를 처리할 때 각 주문마다 상품과 회원 애그리거트를 읽어 온다고하면 N+1문제가 발생한다.

    ~~~java
    Member member = memberRepository.findByOrdererId(ordererId);
    List<Order> order = orderRepository.findByOrdererId(ordererId);
    List<OrderView> dtos = order.stream()
    	.map(order -> {
    		Long productId = order.getOrderLines().get(0).getProductId();
    		//각 주문마다 첫번째 주문 상품 정보 로딩을 위한 쿼리 실행 -> N + 1 문제 발생
    		Product product = productRespository.findById(productId);
    		return new OrderView(order, member, product);
    	}).collcet(toList());
    ~~~

  - ID참조 방식을 사용하면서 N+1 같은 문제가 발생하지 않도록 하려면 조회 전용 쿼리를 사용하면 된다. 

    - 데이터 조회를 위한 별도의 DAO를 만들고 조회 메서드에 해당 id들을 조인 하여 한번의 쿼리로 필요한 데이터를 가져오면 된다.

- 애그리거트마다 서로 다른 저장소를 사용하면 한번의 쿼리로 관련 애그리거트를 조회할 수 없다. 이 때는 조회 성능을 높이기 위해 캐시를 적용하거나, 조회 전용 저장소를 따로 구성한다.
  - 특히 한대의 DB 장비로 대응할 수 없는 수준의 트래픽이 발생하는 경우 캐시나 조회 전용 저장소는 필수로 선택해야 하는 기법이다.



#### 3.5 애그리거트 간 집합 연관

- 애그리거트 간 1-N과 M-N 연관이 존재한다.
  - 연관관계를 맺을 때, 1-N 연관이 있더라도 1에 속해있는 N의 데이터가 수만개 수준으로 많다면 이 코드를 실행할 때마다 실행속도가 급격히 느려져 성능에 심각한 문제를 일으킬 수 있다.
  - 1에 속한 N을 구할 필요가 있다면 N의 입장에서 1에 속한 정보를 찾는 N-1 연관관계로 구현하면 된다.



#### 3.6 애그리거트를 팩토리로 사용하기

- 고객이 특정 상점을 여러 차례 신고해서 해당 상점이 더 이상 물건을 등록하지 못하도록 차단한 상태라고 해보자.

- 상품 등록 기능을 구현한 응용 서비스는 다음과 같이 상점 계정이 차단 상태가아닌 경우에만 상품을 생성하도록 구현할 수 있다.

  ~~~java
  public class RegisterProdcutService {
  	public Long registerProduct(NewProductRequest req) {
  		Store store = storeRepository.findById(req.getStoreId);
      checkNull(store);
      if (store.isBlocked()) {
        throw new StoreBlockedException();
      }
      
      Product product = new Product(store.getId, ...);
      productRepository.save(product);
      return product.getId();
  	}
  }
  ~~~

  - 위 코드의 문제점은 상점이 차단된 상태인지 확인하고, 해당 상점이 상품을 등록할 수 있는 것은 논리적으로 하나의 기능인데, 이 도메인 로직이 응용서비스에 노출 되어 있다.

- 이 도메인 기능을 넣기 위한 별도의 도메인 서비스나 팩토리 클래스를 만들 수도 있지만 이기능을 Store 애그리거트에 구현할 수 있다.

  ```java
  public Product createProduct(ProductInfo productInfo) {
    if (isStoreBlocked()) {
      throw new StoreBlockedException();
    }
    Product product = new Product(productInfo.getName(), productInfo.getAmount(),
        productInfo.getCategoryId(), this);
    products.add(product);
    return product;
  }
  ```

  - Store 애그리거트의 createProduct()는 Product 애그리거트를 생성하는 팩토리 역할을 한다. 팩토리 역할을 하면서도 중요한 도메인 로직을 구현하고 있다. 
  - 팩토리 기능을 구현했으므로, 응용서비스에서는 해당 함수만 호출하여 상품을 생성하면된다.

- 영철님의 설명을 듣고, 난 위의 기능 보다는 도메인 서비스에서 그냥 조합해서 생성하는 것도 좋은 방향이라고 생각하지만, 나중에 실무를 진행하면서 기회가 있다면 위와 같이 한번 시도를 해보고, PR의 리뷰를 맡기겠다.... :)



### 4. 레포지토리 모델구현

#### 4.1 JPA를 이용한 레포지토리 구현

- 애그리거트를 어떤 저장소에 저장햐느냐에 따라 레포지토리를 구현하는 방법이 다르다.
  - 나는 이번장을 쌩 JPA가 아닌 Spring data jpa를 이용해 구현을 진행할 것이다.



#### 4.1.1 모듈 위치

- 레포지토리 인터페이스는 애그리거트와 같이 도메인 영역에 속하고, 리포지토리를 구현한 클래스는 인프라스트럭처 영역에 속한다.

  ![](./img/repository1.jpeg)

- Spring Data JPA는 JpaRepository<Entity, Id>를 extends하면, 자동으로 구현 객체를 생성해준다.



#### 4.1.2 레포지토리

- 레포티조티라 제공하는 기본기능은 다음 두가지이다.
  - ID로 애그리거트 조회하기
  - 애그리거트 저장하기

- 레포지토리 인터페이스는 애그리거트 루트를 기준으로 작성한다.
  - 예를 들면 주문 애그리거트는 Order인 루트 엔티티의 레포지토리만 생성한다.
    - 다른 내부 엔티티인 OrderLine, Orderer, ShippingInfo등의 객체애 대해서는 생성하지 않는다.
  - Spring Data JPA는 `ID로 조회, 저장, 삭제` 기능을 모두 제공한다.

- JPA를 사용하면 수정한 결과를 저장소에 반영하는 메서드를 추가할 필요가 없다.

  - JPA는 트랜잭션 범위에서 변경한 데이터를 자동으로 DB에 반영한다 -> 더티 체킹

    ~~~java
    @Transactional
      public void changeShippingInfo(ChangeOrderShippingInfoCommand changeOrderShippingInfoCommand) {
        Optional<Orders> optionalOrder = orderRepository.findById(
            changeOrderShippingInfoCommand.getOrderId());
        Orders order = optionalOrder.orElseThrow(NoOrderException::new);
        ShippingInfo newShippingInfo = changeOrderShippingInfoCommand.getShippingInfo();
        order.changeShippingInfo(newShippingInfo);
    
        if (changeOrderShippingInfoCommand.isUseNewShippingAddressAsMemberAddress()) {
          Optional<Member> optionalMember = memberRepository.findById(order.getOrderer().getMemberId());
          Member member = optionalMember.orElseThrow(NoSuchElementException::new);
          member.changeAddress(newShippingInfo.getAddress());
        }
      } //메서드가 끝날때 commit을 하면서 더티체킹하여 Update쿼리가 자동으로 반영된다.
    ~~~

- JPA는 ID가 아닌 다른 조건으로 검색할 때, findBy프로퍼티로 조회할 수 있다.

  ~~~java
  public interface OrderRepository extends JpaRepository<Orders, Long> {
  	List<Orders> findByOrdererId(Long ordererId);
  }
  ~~~

  - ID외에 다른조건으로 애그리거트를 조회할 떄 JPA Crietria(비추)나, JPQL 또는 QueryDsl을 사용할 수 있다.

>  Tip. 삭제 기능
>
> 삭제 요구사항이 있더라도 데이터를 실제 삭제하는 경우는 많지 않다. 관리자 기능에서 삭제한 데이터를 조회해야 하는 경우도 있고, 데이터 원복을 위해 일정기간동안 보관해야할 때도 있기 때문이다. 이런경우에는 삭제플래그를 두어 사용자에게 해당 데이터를 보여주지 않는다.



#### 4.2 스프링 데이터 JPA를 이용한 레포지토리 구현

- 4.1에서 설명한바와 같이 스프링 데이터 JPA는 지정한 규칙에 맞게 레포지토리 인터페이스를 정의하면 레포지토리를 구현한 객체를 알아서 만들어 스프링 빈으로 등록해준다.

- 스프링 데이터 JPA는 다음 규칙에 따라 작성한 인터페이스를 찾아서 인터페이스를 구현한 스프링 빈 객체를 자동으로 등록한다.

  - org.springframework.data.jpa.repository<T, ID>
  - T는 엔티티 타입을 지정하고, ID는 식별자 타입을 지정한다.

    - 예) Order 엔티티를 위한 OrderRepository

      ~~~java
      //엔티티
      @Entity(name = "order")
      @Getter
      public class Orders {
      
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String orderNumber;
        @Enumerated(value = EnumType.STRING)
        private OrderState orderState;
        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "receiver.name",
                column = @Column(name = "receiver_name")),
            @AttributeOverride(name = "receiver.phoneNumber",
                column = @Column(name = "receiver_phone_number"))
        })
        private ShippingInfo shippingInfo;
        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
        List<OrderLine> orderLines;
        @Embedded
        @AttributeOverride(name = "value", column = @Column(name = "total_amounts"))
        private Money totalAmounts;
        @Embedded
        private Orderer orderer;
      	
        ....
      }
      
      ===================================================
      
      //레포지토리
      public interface OrderRepository extends JpaRepository<Orders, Long> {
      	List<Orders> findByOrdererId(Long ordererId);
      }
      ~~~

      

#### 4.3 매핑 구현

#### 4.3.1 엔티티와 밸류 기본 매핑 구현

- 애그리거트와 JPA 매핑을 위한 기본 규칙은 다음과 같다.
  - 애그리거트 루트는 엔티티이므로 @Entity로 매핑 설정한다.
- 한 테이블에 엔티티와 밸류 데이터가 같이 있다면
  - 밸류는 @Embeddable로 매핑 설정한다.
  - 밸류 타입 프로퍼티는 @Embedded로 매핑 설정한다.

- 주문 애그리거트를 예로 들어보자면 루트 엔티티는 Order이고, 이 애그리거트에 속한 Orderer, Money, ShippingInfo는 밸류이다.

  - 루트 엔티티와 루트 엔티티에 속한 밸류는 한테이블에 매핑할 때가 많다.

  <img src="./img/repository2.jpeg" alt="repository2" style="zoom:40%;" />

  - 주문 애그리거트에서 루트 엔티티인 Order는 @Entity로 매핑한다.

  ~~~java
  //엔티티
  @Entity(name = "order")
  @Getter
  public class Orders {
  ...
  }
  ~~~

  - Order에 속하는 Orderer는 밸류이므로 @Embeddable로 매핑한다.

  ~~~java
  @Getter
  @Embeddable
  @AllArgsConstructor
  @NoArgsConstructor
  public class Orderer {
  
    private Long memberId;
    private String name;
    private String phoneNumber;
    private String email;
  }
  ~~~

  - Orderder의 MemberId는 Member 애그리거트를 id로 참조한다.

  > 책에서는 Orderer의 memberId를 orderer_id로 설정하고 Member의 Id를 orderer_id로 했으나, 나는 그렇게 까지 바꿀 필요성이 있나라는 생각이 들어서 책과 같이 구현하지 않았다. Orderer의 meberId를 orderer_id로 바꾸는 것 까지는 가능하다고 생각한, Member는 회원정보등이 포함되어있는데, 단순히 orderer_id라고 하기에는 의미가 애매해서 그렇게 결정했다.

   - Order의는 ShippingInfo 밸류의 Receiver 밸류의 name, phone_number 컬럼의 의미 전달을 위해 @AttributeOverride를 통해 컬럼이름을 커스터마이징 해주었다.

     ~~~java
     @Entity(name = "order")
     @Getter
     public class Orders { 
       //...
       @Embedded
       @AttributeOverrides({
           @AttributeOverride(name = "receiver.name",
               column = @Column(name = "receiver_name")),
           @AttributeOverride(name = "receiver.phoneNumber",
               column = @Column(name = "receiver_phone_number"))
       })
       private ShippingInfo shippingInfo;
     }
     ~~~



#### 4.3.2 기본 생성자

- 엔티티와 밸류의 생성자는 객체를 생성할 때 필요한 것을 전달받는다.

  - 예를 들면 Receiver 밸류 타입은 생성시점에 수취인 이름과 전화번호를 생성자 파라미터로 전달받는다.

    ~~~java
    @AllArgsConstructor //lombok을 이용한 모든 파라미터를 받는 생성자
    @NoArgsConstructor //lombok을 이용한 기본 생성자
    public class Receiver {
      private String name;
      private String phoneNumber;
    }
    ~~~

  - Receiver가 불변 타입이면 생성 시점에 필요한 값을 모두 전달받으므로 값을 변경하는 set 메서드를 제공하지 않는다.
  - 하지만 JPA에서 @Entity와 @Embeddable로 클래스를 매핑하려면 기본 생성자를 제공해야한다.
    - DB에서 데이터를 읽어와 매핑된 객체를 생성할 때 기본생성자를 사용해서 객채를 생성하기 때문이다.



#### 4.3.3 필드 접근 방식 사용

- JPA는 필드와 메서드의 두가지 방식으로 매핑을 처리할 수 있다. 메서드 방식을 사용하려면 프로퍼티를 위한 get/set 메서드를 구현해야한다.
  - 엔티티에 프로퍼티를 위한 공개 get/set 메서드를 추가하면 도메인의 의도가 사라지고 객체가 아닌 데이터 기반으로 엔티티를 구현할 가능성이 높아진다. 
  - 특히 set 메서드는 내부 데이터를 외부에서 변경할 수 있는 수단이 되기 때문에 캡슐화를 깨는 원인이 될 수 있다.
  - 엔티티가 객체로서 제 역할을 하려면 외부에서 set 메서드 대신 의도가 잘 드러나는 기능을 제공한다.
    - 예를 들면 setShippingInfo() 보단 배송지를 변경한다는 의미의 changeShippinInfo가 더 알맞을 것이다.



#### 4.3.4 AttributeConverter를 이용한 밸류 매핑 처리

- Int, long, String, LocalDate와 같은 타입은 DB 테이블의 한 개 칼럼에 매핑된다. 이와 비슷하게 밸류 타입의 프로퍼티를 한 개 컬럼에 매핑해야할 때도 있다.

  - 예를 들면 Money가 돈의 값과 통화라는 두 프로퍼티를 갖고 있는데 DB 테이블에 한 개 컬럼에 "1000₩, 1000$" 와 같은 형식을 저장할 수 있다.

    ~~~java
    public class Money {
    	private int amount;
    	private String currency;
    }
    
    // DB 저장시 "1000₩, 1000$"로 저장 -> money varchar(20)
    ~~~

  - 두개 이상의 프로퍼티를 가진 밸류 타입을 한 개 컬럼에 매핑하려면 @Embeddable 애노테이션으로 처리할 수 없다. 이럴 때 사용하는 것이 AttributeConverter이다.

- AttributeConverter는 다음과 같이 밸류 타입과 컬럼 데이터간의 변환을 처리하기 위한 기능을 정의하고 있다.

```
package javax.persistence;

public interface AttributeConverter<X,Y> {


    public Y convertToDatabaseColumn (X attribute);


    public X convertToEntityAttribute (Y dbData);
}
```

- 타입 파라미터 X는 벨류타입이고, Y는 DB 타입이다. 
  - convertToDatabaseColumn() 메서드는 밸류 타입을 DB 컬럼 값으로 변환하는 기능을 구현한다.
  - convertToEntityAttribute() 메서드는 DB 컬럼값을 밸류 타입으로 변환하는 기능을 구현한다.



```java
package com.example.ddd_start.infrastructure.money.moeny_converter;

import com.example.ddd_start.common.domain.Money;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = ture)
public class MoneyConverter implements AttributeConverter<Money, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Money money) {
    return money == null ? null : money.getValue();
  }

  @Override
  public Money convertToEntityAttribute(Integer value) {
    return value == null ? null : new Money(value);
  }
}
```

> 이패키지가 인프라스트럭쳐에 있는 것이 맞는것인가?? 특정 기술에 대한 구현체여서 맞는것 같기도 하고, 또 도메인에만 종속되서 아닌 것 같기도 하다.

- AttributeConverter 인터페이스를 구현한 클래스는 @Converter 애너테이션을 적용한다.

- AutoApply 속성값을 보면 이 속성이 true로 지정하면 모델에 출현하는 모든 Money 타입의 프로퍼티에 대해 MoneyConverter를 자동으로 적용한다.

  - 예) Order의 Money

    ~~~java
    @Entity(name = "order")
    @Getter
    public class Orders {
    	@Column(name = "total_amounts")
    	@Convert(converter = MoneyConverter.class) // autoApply값이 true이면 자동으로 @Convert를 지정하지 않아도 MoneyConverter를 적용해서 값 변환
    	private Money money;
    }
    ~~~



#### 4.3.5 밸류 컬렉션: 별도 테이블 매핑

- Order 엔티티는 한 개 이상의 OrderLine을 가질 수 있다. OrderLine에 순서가 있다면 다음과 같이 List타입을 이용해서 컬렉션을 프로퍼티로 지정할 수 있다.
- 나는 처음 설계를 할 때 부터 OrderLine은 엔티티로 매핑을 하였다. 허나 책에서는 테이블로 매핑을 하였다.

​	<img src="./img/repository3.jpeg" alt="repository3" style="zoom:33%;" />

- 밸류 컬렉션을 저장하는 ORDER_LINE 테이블은 외래키를 이용해서 엔티티에 해당하는 ordersId를 참조한다. 이 외래키는 컬렉션이 속할 엔티티를 의미한다.
- 밸류 컬렉션을 별도 테이블로 매핑할 때는 @ElmentCollection과 @CollectionTable을 함께 사용한다.
- List 타입은 자체적으로 인덱스를 가지고 있다.
  - @OrderColumn 애너테이션을 이용해서 지정한 칼럼에 리스트의 인덱스 값을 설정할 수 있다.
  - @CollectionTable은 밸류를 저장할 테이블을 지정한다. name 속성은 테이블 이름을 지정하고 joinColumns 속성은 외부키로 사용할 컬럼을 지정한다. 두개 이상인 경우 @JoinColumn의 배열을 이용해서 외부키 목록을 지정한다.



#### 4.3.6 밸류 컬렉션: 한 개 컬럼 매핑

- 밸류 컬렉션을 별도 테이블이 아닌 한 개 컬럼에 저장해햐 할 때가 있다. 
  - 예를 들어 도메인 모델에는 이메일 주소목록을 Set으로 보관하고 DB에는 한개 컬럼에 콤마로 구분해서 저장해야 할때가있다.
  - 이 때 AttributeConverter를 사용하면 밸류 컬렉션을 한개 컬럼에 쉽게 매핑할 수 있다. 단 AttributeConverter를 사용하려면 밸류 컬렉션을 표현하는 새로운 밸류타입을 추가해야 한다.



#### 4.3.7 밸류를 이용한 ID 매핑

- 식별자라는 의미를 부각시키기 위해 실별자 자체를 밸류타입으로 만들 수도 있다.

  - 밸류 타입을 식별자로 매핑하면 @Id 대신 @EmbeddedId 애너테이션을 사용한다.

    ~~~java
    public class OrderLine {
    	@EmbeddedId
      private OrderProductId orderProductId;
    }
    
    public class OrderProductId implements Serializable{ 
      Long orderId;
      Long productId;
    }
    ~~~

  - JPA에서 식별자 타입은 Serializable 타입이어야 하므로 식별자로 사용할 밸류타입은 Serializable 인터페이스를 상속 받아야 한다.

  - 밸류 타입으로 식별자를 구현할 때 얻을 수 있는 장점은 실벼자에 기능을 추가할 수 있다는 점이다.

    - 예시
    - 1세데 시스템과 2세대 시스템의 주문번호를 구분할 때 주문번호의 첫글자를 이용할 경우



#### 4.3.8 별도 테이블에 저장하는 밸류 매핑

- **애그리거트에서 루트 엔티티를 뺀 나머지 구성요소는 대부분 밸류이다. 루트 엔티티외에 다른 엔티티가 있다면 진짜 엔티티인지 의심해봐야 한다.** 

  - 단지 별도 테이블에 데이터를 저장한다고 해서 엔티티인 것은 아니다. 주문 애그리거트도 OrderLine을 별도 테이블에 저장하지만 OrderLine 자체는 엔티티가 아니라 밸류이다.

- **밸류가 아니라 엔티티가 확실하다면 해당 엔티티가 다른 애그리거트는 아닌지 확인해야 한다.**

  - 특히 자신만의 독자적인 라이플 사이클을 갖는다면 구분되는 애그리거트일 가능성이 높다.

  - 상품과 리뷰가 대표적인데 상품 상세화면을 보여줄 때 고객 리뷰가 포함된다고 생각할 수 있다.

    - 하지만 Product와 Reivew는 함께 생성되지 않고 함께 변경되지도 않는다.
    - 또한 두개의 객체를 생성하는 주체가 Product = 상점, Review = 고객으로 다르다.
    - 그러므로 Review는 엔티티가 맞지만 리뷰 애그리거트에 속한 엔티티이지 상품에 속한 엔티티가 아니다.

  - 애그리거트에 속한 객체가 밸류인지 엔티티인지 구분하는 방법은 고유 식별자는 갖는지 확인하는 것이다.

  - 하지만 식별자를 찾을 때 매핑되는 테이블의 식별자를 애그리거트 구성요소의 식별자와 동일한 것으로 착각하면 안된다.

    - Article과 ArticleContent를 보면 알 수 있듯이 ArticleContent의 식별자는 그저 Article에 속해있는 것을 표현하기에 단순히 밸류로 보는게 더 맞을 것이다.

    - 관계를 정확하게 맞으면 아래와 같이 맺어질 것이다.

      <img src="./img/repository4.jpeg" alt="repository4" style="zoom:100%;" />

  - 이런 경우 ArticleContent를 다른 테이블로 저장하고 싶다면 @SencondaryTable을 이용한다.

    - name 속성은 밸류를 저장할 테이블을 지정한다.

    - pkJoinColumns 속성은 밸류 테이블에서 엔티티 테이블로 조인할 때 사용할 컬럼을 지정한다.

    - @AttriubuteOverride를 이용하여 해당 밸류 데이터가 저장될 테이블 이름을 지정하면 된다.

      ~~~java
      @SecondaryTable(name ="밸류를 저장할 테이블 이름")
      public class Atricle{
      
      @Embedded
      @AttriubuteOverride(
      name = "content",
      column = @Column(table = "article_content", name = "content"))
      private ArticleContent articleContent;
      }
      ~~~


    - 허나 게시글 목록을 보일 땐 Article만 보이면 되는데, @SeconderyTable을 사용하면 조인 해서 가져온다. 이럴 때는 밸류를 엔티티로 설정하고 지연 로딩 방식을 설정할 수 있다고 하는데, 책에서는 비추를 한다.



#### 4.3.9 밸류 컬렉션을 @Entity로 매핑하기

- 개념젹으로 밸류인데 구현 기술의 한계나 팀 표준에 의해 @Entity를 사용해야할 때가 있다.

- JPA는 @Embeddable 타입의 클래스 상속 매핑을 지원하지 않는다.

  ![repository5](./img/repository5.jpeg)

  - 상속 구조를 갖는 밸류타입을 사용하려면 @Embeddable 대신 @Entity를 이용해서 상속 매핑으로 처리해야 한다.

  - 밸류 타입을 @Entity로 매핑으로 식별자 매핑을 위한 필드도 추가해야 한다.

    ![repository6](./img/repository6.jpeg)

  - 한 테이블에 Image와 그 하위 클래스를 매핑하므로 Image 클래스에 다음 설정을 사용한다.

    - @Inheritance 에너테이션 적용
    - strategy 값으로 SINGLE_TABLE 사용
    - @DiscriminatorColumn 애너테이션을 이용하여 타입 구분용으로 사용할 칼럼 지정

  - 상위 클래스인 Image를 추상 클래스로 구현

  ```java
  @Entity
  @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
  @DiscriminatorColumn(name = "image_type")
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Table(name = "image")
  public abstract class Image {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    Long id;
  
    @Column(name = "image_path")
    private String path;
    
  	@Temporal(TemporalType.TIMESTAMP)
      private Instant uploadTime;
  ```

    public Image(String path, Instant uploadTime) {
      this.path = path;
      this.uploadTime = uploadTime;
    }
      
    protected String getPath() {
      return path;
    }
      
    public Instant getUploadTime() {
      return uploadTime;
    }
      
    public abstract String getURL();
      
    public abstract boolean hasThumbnail();
      
    public abstract String getThumbnailURL();
     - Image를 상속받는 InternalImage와 ExternalImage를 구현하였다.
  
      }
  
  ```


  ```java
  @Entity
  @DiscriminatorValue("II")
  @NoArgsConstructor
  public class InternalImage extends Image {
  
    private String thumbnailURL;
  
    public InternalImage(String path, Instant uploadTime, String thumbnailURL) {
      super(path, uploadTime);
      this.thumbnailURL = thumbnailURL;
    }
  
    @Override
    public String getURL() {
      return this.getURL();
    }
  
    @Override
    public boolean hasThumbnail() {
      return thumbnailURL != null;
    }
  
    @Override
    public String getThumbnailURL() {
      if (hasThumbnail()) {
        return this.thumbnailURL;
      }
      throw new NoSuchElementException();
    }
  }
  ```

  ```java
  @Entity
  @DiscriminatorValue("DI")
  @NoArgsConstructor
  public class ExternalImage extends Image {
  
    private String thumbnailURL;
  
    public ExternalImage(String path, Instant uploadTime, String thumbnailURL) {
      super(path, uploadTime);
      this.thumbnailURL = thumbnailURL;
    }
  
    @Override
    public String getURL() {
      return this.getPath();
    }
  
    @Override
    public boolean hasThumbnail() {
      return thumbnailURL != null;
    }
  
    @Override
    public String getThumbnailURL() {
      if (hasThumbnail()) {
        return this.thumbnailURL;
      }
      throw new NoSuchElementException();
    }
  }
  ```

  - Image가 @Entity 이므로 목록을 담고 있는 Product는 @OneToMany를 이용해서 매핑을 처리하며, 상품이 저장될 때나 삭제될 때 영속성이 전이 되게 persist와 remove를 활성화 해주고, 상품이 삭제되면 이미지는 고아객체가 되므로 고아객체 제거를 허용하기 위해 orphanRemoval를 true로 설정해준다.

    ```java
    @Entity
    @Getter
    @NoArgsConstructor
    public class Product {
    	...
    
      @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
          orphanRemoval = true,
      mappedBy = "product")
      private List<Image> images = new ArrayList<>();
    
      public void changeImages(List<Image> newImages) {
        images.clear();
        images.addAll(newImages);
      }
    }
    ```

  - @Entity의 List의 clear를 호출할 때 select쿼리로 대상 엔티티를 로딩하고, 각 개별 엔티티에 대해 delete 쿼리를 실행한다.

    - 이미지가 4개라고 가정하면 상품 정보를 가져올 때 쿼리가 한번 호출되고, clear를 호출할 때 이미지 4개에 대한 쿼리가 각각 호출되어 성능에 문제가 생길 수 있다.

  - 대신 @Embeddable 타입에 대한 컬렉션의 clear() 메서드를 호출하면 컬렉션에 속한 객체를 로딩하지 않고 한번의 delete 쿼리로 삭제처리를 수행한다.

    - 따라서 애그리거트의 특성을 유지하면서 이문제를 해소하려면 결국 상속을 포기하고 @Embeddable로 매핑된 단일 클래스로 구현해야 한다.

    - 예시

      ~~~
      @Embeddable
      public class Image{
      	private String imageType;
      	private String path;
      	@Temporal(TemporalType.TIMESTAMP)
      	private Instant uploadTime;
      	
      	public boolean hasThumbnail(){
      		return this.imageType.equals("II");
      	}
      }
      ~~~

    - 코드 유지보수와 성능의 두가지 측면을 고려해서 구현방식을 선택해야 한다.



#### 4.3.10 ID참조와 조인 테이블을 이용한 단방향 M-N 매핑

- 애그리거트 간 집합 연관은 성능 상의 이유로 피해야한다.

  - 그럼에도 불구하고 요구사항을 구현하는데 집합 연관을 사용하는 것이 유리하다면 ID 참조를 이용한 단방향 집합 연관을 적용해 볼 수 있다.

    ~~~java
    @Entity
    public class Product {
    	@Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    	private Long id;
    	
    	@ElementCollection
    	@CollectionTable(name = "product_category", 
    	joinColumns = @JoinColumn(name = "product_id"))
    	private Set<CategoryId> categoryIds;
    }
    ~~~

    - 위 코드는 Product에서 Category로 단방향 M-N 연관을 ID 참조 방식으로 구현한 것이다.
    - ID 참조를 이용한 애그리거트 간 단방향 M-N 연관은 밸류 컬렉션 매핑과 동일한 방식으로 설정되었다.
    - 차이점이 있다면 집합의 값에 밸류 대신 연관을 맺는 식별자가 온다.
    - @ElementCollection을 이용하기 때문에 Product를 삭제할 때 매핑에 사용한 조인테이블의 데이터도 함께 삭제된다.



#### 4.4 애그리거트 로딩 전략

- 매핑을 설정할 때 애그리거트에 속한 객체가 모두 모여야 완전한 하나가 된다.
  - 애그리거트 루트를 로딩하면 루트에 속한 모든 객체가 완전한 상태여야 함을 의미한다.

- 조회 시점에서 애그리거트를 완전한 상태가 되도록 하면 즉시로딩을 하면 된다.
  - @ManyToOne(fetch = FetchType.EAGER)
  - 즉시 로딩 방식을 설정하면 애그리거트 루트를 로딩하는 시점에 모두 로딩할 수 있어 좋지만 장점만 있지 않다.
    - 예상치 못한 쿼리가 발생할 수 있다.
    - 예를 들자면 @OneToMany가 걸려있는 연관관계의 데이터를 즉시 가져올 때 카타시안 조인이 발생하면서 객체의 개수가 많으면 쿼리의 개수가 객체수 만큼 곱해진다. (상품 1개, 이미지 10개, 옵션 10개 = 1 * 10 * 10 = 100)
    - 이렇듯 데이터의 개수가 많아지면 성능(실행 빈도, 트래픽, 지연 로딩시 실행속도 등)을 검토해봐야 한다.

- 애그리거트는 개념적으로 하나여야 한다. 하지만 루트 엔티티를 로딩하는 시점에 애그리거트에 속한 객체 모두를 로딩해야 하는 것은 아니다.

  - 애그리거트가 완전해야 하는 이유

    - 상태를 변경하는 기능을 실행할 때 애그리거트 상태가 완전해야한다.
    - 표현 영역에서 애그리거트의 상태 정보를 보여줄 때 필요하다.
      - 별도의 조회 전용 기능과 모델을 구현하는 방식(VO?, DTO?)을 사용하는 것이 더 유리하다.

  - 애그리거트의 완전한 로딩과 관련된 문제는 상태변경과 더 관련이 있다. 

  - 상태변경 기능을 실행하기 위해서는 완전한 로딩 상태가 필요없다. 왜냐하면 JPA는 트랜잭션 범위 내에서 지연 로딩을 허용한다.

    ~~~java
    @Service
    @RequiredArgsConstructor
    public class DeleteProductService {
    
      private final ProductRepository productRepository;
    
      public void removeOptions(Long productId, int optIdx) {
        //Option은 LAZY로 즉시 로딩 안됨.
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchElementException::new);
        //여기서 지연로딩이 된다.
        product.removeOption(optIdx);
      }
    }
    ~~~

  - 지연로딩의 장점은 동작 방식이 항상 동일하기 때문에 경우의 수를 따질 필요가 없다.

  - 지연 로딩은 즉시로딩보다 쿼리 실행횟수가 많아질 가능성이 더 높다(N+1) -> 이럴 땐 fetchJoin을 해서 즉시로딩 하면 되는데, 보통 성능을 튜닝 할 때는 지연로딩으로 설정하고 튜닝을 진행한다.

  - 각 상황에 알맞게 로딩방식을 사용하면 될 것 같다.



#### 4.5 애그리거트 영속성 전파

- 애그리거트가 완전한 상태여야 한다는 것은 애그리거트를 조회할 때 뿐 아니라 저장 및 삭제할 때도 하나로 처리해야함을 의미한다.
  - 저장 메서드는 애그리거트 루트 포함 애그리거트에 속해있는 모든 객체가 저장되어야 한다.
  - 삭제 메서드는 애그리거트 루트 포함 애그리거트에 속해있는 모든 객체가 삭제되어야 한다.
- @Embeddable 매핑 타입은 함께 저장되고 삭제되므로 cascde 설정을 하지 않아도 된다.
- 반면에 @Entity 타입에 대한 매핑은 cascade를 통해 영속성 전파를 설정해줘야 한다.
  - CascadeType.PERSIST(저장), CascadeType.REMOVE(삭제)
  - orphanRemoval = true -> true이면 고아객체도 삭제한다를 의미



#### 4.6 식별자 생성 기능

- 식별자는 크게 세가지 방식중 하나로 생성한다.
  - 사용자가 직접 생성 (이메일 주소)
  - 도메인 로직으로 생성 (orderNumber)
  - DB를 이용한 일련번호 사용(오토 인크리먼트, (오라클, 포스트그레) - 시퀀스)
- 식별자 생성 규칙이 있다면 엔티티를 생성은 도메인 규칙이므로 별도의 도메인 서비스로 분리한다.
  - 특정값을 조합하는 식별자도 포함된다 -> orderNumber 또는 날짜를 조합해서 만드는 번호 등
  - 식별자 생성 규칙을 구현하기에 적합한 또 다른 장소는 레포지토리이다.
    - 레포지토리 인터페이스에 식별자를 생성하는 메서드를 추가하고 리포지토리 구현 클래스에 알맞게 구현하면된다.(Spring Data JPA를 사용하면 딱히 쓸일은 없을 것 같다.)
  - DB 자동 증가 칼럼은 @GeneratedValue(strategy = GenerationType.IDENTITY(오토 인크리먼트), GenerationType.SEQUENCE(시퀀스))
    - 자동 증가 컬럼은 식별자를 DB insert 쿼리를 실행할 때 생성되므로 레포지토리에 객체를 저장이후에 알 수 있다.



#### 4.7 도메인 구현과 DIP

- 이장에서 구현된 리포지토리는 DIP 원칙을 어기고 있다.

  - 엔티티에서는 구현기술인 JPA에 특화된 @Entity, @Table, @Id, @Column등의 애너테이션을 사용한다.

  - DIP에 따르면 @Entity, @Table은 구현기술에 속하므로 도메인 모델은 구현모델인 JPA에 의존하지 말아야 한다.

  - Repository 인터페이스도 JPA의 구현기술인 JpaRepository를 상속받는다. 

  - 구현 기술에 의존 없이 도메인을 순수하게 유지하려면 아래의 그림과 같이 구현해야 한다.

    ![](./img/repository7.jpeg)

  - 위의 사진의 구조처럼 구현한다면 도메인이 받는 영향을 최소화 할 수 있다.
  - DIP를 적용하는 주된 이유는 저수준 구현이 변경되더라도 고수준이 영향을 받지 않도록 하기 위함이다. 하지만 레포지토리와 도메인 모델의 구현기술은 거의 바뀌지 않는다.
  - 개발의 편의성과 실용성을 가져가기 위해 기술에 따른 구현 제약이 낮다면 합리적인 선택이 될 수 있다.



### 5. 스프링 데이터 JPA를 이용한 조회 기능

#### 5.1 시작에 앞서

- CQRS는 명령(Command) 모델과 조회(Query)모델을 분리하는 패턴이다.
  - 명령 모델은 상태를 변경하는 기능을 구현할 때 사용한다.
    - 예) 회원 가입, 암호 변경 등 처럼 상태(데이터)를 변경하는  기능
  - 조회 모델은 데이터를 조회하는 기능을 구현할 때 사용한다.
    - 예) 주문 목록 조회, 주문 상세 조회(테이터)를 조회하는 기능
- 엔티티, 애그리거트, 레피지토리 등의 모델은 상태를 변경할 때 주로 사용된다. 즉 도메인 모델은 명령 모델로 주로 사용된다.



#### 5.2 검색을 위한 스펙

- 검색 조건이 고정되어 있고 단순하면 특정 조건으로 조회하는 기능을 만들면 된다.

  ~~~java
  @Repository
  public interface OrderRepository extends JpaRepository<Orders, Long> {
  
    List<Orders> findAllByIdAndCreatedAtBetween(Long id, Instant startAt, Instant endedAt);
  }
  ~~~

- 목록 조회와 같은 기능은 다양한 검색 조건을 조합해야 할 때가 있다. 필요한 조합마다 find 메서드를 정의할 수도 있지만 이것은 좋은 방법이 아니다. 조합이 증가할수록 정의해야 할 find 메서드도 함께 증가한다.

  > 책에서는 Spectificaiton을 쓰는 방법을 통해 동적으로 조건을 거는 법을 서술했으나, 나는 Querydsl을 써서 구현할 예정이다.

- 레포지토리나 DAO는 검색 대상을 걸러내는 용도로 스펙을 사용한다. 레포지토리가 스펙을 이용해서 검색 대상을 걸러주므로 특정 조건을 충족하는 애그리거트를 찾고 싶으면 스펙을 생성해서 레포지토리에 전달해주기만 하면 된다.
- 하지만 실제 스펙은 이렇게 구현하지 않는다. 모든 애그리거트 객체를 메모리에 보관하기도 어렵고, 설사 메모리에 다 보관할 수 있다 하더라도 조회 성능에 심각한 문제가 발생한다.
  - 실제 스펙은 사용하는 기술에 맞춰 구현하게 된다.



#### 5.3 스프링 데이터 JPA를 이용한 스펙 구현

- 책에서는 Criteria를 사용해서 구현하는데 이것은 개발을 진행하는데 비추되므로 QueryDsl을 사용해서 구현할 것이다.

  - ordererId를 이용한 orderDto조회

  ~~~java
  public class OrderCustomRepositoryImpl implements OrderCustomRepository {
  
    private final JPAQueryFactory queryFactory;
  
    public OrderCustomRepositoryImpl(EntityManager em) {
      this.queryFactory = new JPAQueryFactory(em);
    }
  
    @Override
  
    public List<OrderDto> findOrderByOrdererId(OrderSearchCondition orderSearchCondition) {
      List<OrderDto> result = queryFactory.query()
          .select(Projections.constructor(OrderDto.class,
              order.orderNumber,
              order.orderState,
              order.shippingInfo,
              order.totalAmounts,
              order.orderer.name,
              order.createdAt
          ))
          .from(order)
          .where(ordererIdEq(orderSearchCondition.getOrdererId()))
          .fetch();
  
      return result;
    }
  
    private BooleanExpression ordererIdEq(Long ordererId) {
      return ordererId == null ? null : order.orderer.memberId.eq(ordererId);
    }
  }
  ~~~
  
  

#### 5.4 레포지토리/DAO에서 스펙 사용하기

- 스펙을 충족하는 엔티티를 검색하고 싶다면 findAll()메소드를 사용하면 된다.
  - findAll()메서드는 스펙 인터페이스를 파라미터로 갖는다.



#### 5.5 스펙 조합

- 스프링 데이터 JPA가 제공하는 스펙 인터페이스는 스펙을 조합할 수 있는 메소드를 제공한다(and와 or 제공). 허나 Querydsl도 where절에 and(), or()메소드를 제공하고 있다.

- Querydsl의 and(), or()메소드

  ~~~java
  @Override
  public List<OrderDto> findOrderByOrdererIdAndOrderStateShipping(
    OrderSearchCondition orderSearchCondition) {
    List<OrderDto> result = queryFactory.query()
      .select(Projections.constructor(OrderDto.class,
  				order.orderNumber,
  				order.orderState,
  				order.shippingInfo,
          order.totalAmounts,
          order.orderer.name,
          order.createdAt))
      .from(order)
      .where(ordererIdEq(orderSearchCondition.getOrdererId())
             .and(orderStateEq(OrderState.SHIPPED)))
      .fetch();
  
    return result;
  }
  ~~~

  

#### 5.6 정렬 지정하기

- 스프링 데이터 JPA는 두 가지 방법을 사용해서 정렬을 지정할 수 있다.

  - 메서드 이름에 OrderBy를 사용해서 정렬 기준 지정

  - Sort를 인자로 전달

- 특정 프로퍼티로 조회하는 find 메서드 이름 뒤에 OrderBy를 사용해서 정렬 순서를 지정할 수 있다.

  ~~~java
  List<Order> findOrderByIdOrderByCreatedAtDesc(Long id);
  ~~~

  - Id 프로퍼티 값을 기준으로 검색조건 지정
  - createdAt 프로퍼티 값 역순으로 정렬 (최신 데이터부터 출력함)

- 두개 이상의 프로퍼티에 대한 정렬 순서를 지정할수도 있다.

  ~~~java
  List<Order> findOrderByIdOrderByCreatedAtDescTotalAmountsDesc(Long id);
  ~~~

  - createdAt 프로퍼티값 역순, 주문 총금액 역순으로 정렬

- 메서드 이름에 OrderBy를 사용하는 방법은 간단하지만 정렬 기준 프로퍼티가 두 개 이상이면 메서드 이름이 길어지는 단점이 있다.

  - 또한 메서드 이름으로 정렬 순서가 정해지기 때문에 상황에 따라 정렬 순서를 변경할 수도 없다.
  - 이럴때는 Sort 타입을 사용하면 된다.

- 스프링 데이터 JPA는 정렬 순서를 지정할 때 사용할 수 있는 Sort 타입을 제공한다.

  ~~~java
  List<Order> findOrderByOrdererId(@Param("OrdererId") Long id, Sort sort);
  ~~~

  - sort 단일 및 다중 설정하는 방법

  ~~~java
  // 단일
  Sort sort = Sort.by("createdAt").ascending();
  
  // 다중
  Sort sort1 = Sort.by("createdAt").ascending();
  Sort sort2 = Sort.by("totalAmounts").ascending();
  Sort sort = sort1.and(sort2);
  	// 또는
  Sort sort = Sort.by("createdAt").descending().and(Sort.by("totalAmounts").ascending());
  ~~~

**Querydsl의 정렬 방법**

- orderBy(): 정렬 메소드

- 정렬하고 싶은 필드를 파라미터로 넘기면 된다

   `.orderBy(member.age.desc())`

   `.orderBy(member.age.asc())`



#### 5.7 페이징 처리하기

- 목록을 보여줄 때 전체 데이터 중 일부만 보여주는 페이징처리는 기본이다.

- 스프링 데이터 JPA는 페이징 처리를 위해 Pageable 타입을 이요한다. Sort 타입과 마찬가지로 findAll()메서드에 Pageable 타입 파라미터를 사용하면 페이징을 자동으로 처리해준다.

  ~~~java
  public interface MemberRepository extends JpaRepository<Member, Long> {
  
    List<Member> findMemberByNameLike(String name, Pageable pageable);
  
  }
  ~~~

  - findByNameLike() 메서드의 마지막 파라미터로 Pageable 타입을 갖는다.
  - Pageable 타입은 인터페이스로 실제 Pageable 타입 객체는 PageRequest 클래스를 이용해서 생성한다.
  - findMemberByNameLike() 메서드를 호출하는 예

  ~~~java
  // 첫번째 파라미터는 page, 두번째 파라미터는 size
  // 아래의 함수는 0번째 page 부터 10개씩 가져온다는 것을 의미한다.
  PageRequest pageRequest = PageRequest.of(0, 10);
  List<Member> memberByNameLike = memberRepository.findMemberByNameLike(name, pageRequest);
  ~~~

- PageReuqest와 Sort를 사용하면 정렬 순서를 지정할 수 있다.

  ~~~java
  Sort sort = Sort.by("id").descending();
  PageRequest pageRequest = PageRequest.of(0, 10, sort);
  ~~~

- Page 타입을 사용하면 데이터 목록뿐만 아니라 조건에 해당하는 전체 개수도 구할 수 있다.

  ~~~java
  Page<Member> findPageMemberByNameLike(String name, Pageable pageable);
  ~~~

  - Pageable을 사용하는 메서드의 리턴 타입이 Page일 경우 스프링 데이터 JPA는 목록 조회 쿼리와 함께 COUNT 쿼리도 실행해서 조건에 해당하는 데이터 개수를 구한다.
  - Page는 전체 개수, 페이지 개수 등 페이징 처리에 필요한 데이터도 함께 제공한다.

- findAll() 메서드도 Pageable을 사용할 수 있다.

- 처음부터 N개의 데이터가 필요하다면 Pageable을 사용하지않고 findFirstN 형식의 메서드를 사용할 수도 있다.

  ~~~java
  List<Member> findFirst3ByNameLikeOrderByName(String name);
  ~~~

  - First대신 Top을 사용해도 문제 없다. Fisrt나 Top 뒤에 숫자가 없으면 한개 결과만 리턴한다.

  ~~~java
  List<Member> findTop3ByNameLikeOrderByName(String name);
  ~~~



**Querydsl의 페이징**

~~~java
from(Entity)
 .offset(pageable.getOffset())
 .limit(pageable.getPageSize())
~~~

- 조회 메서드를 추가할때 offset은 몇번째 데이터부터 가져올지를 결정하고, limit은 몇개의 데이터를 가져올지를 정한다.



#### 5.8 스펙 조합을 위한 스펙 빌더 클래스

- criteria부분은 스킵하고, Querydsl에서의 BooleanBuilder를 생성하는 법을 정리해 두겠다.

~~~java
 @Override
  public List<OrderDto> searchMyStateOrders(
      OrderSearchCondition orderSearchCondition) {
    //builder 생성
    BooleanBuilder builder = new BooleanBuilder();
    if (orderSearchCondition.getOrdererId() != null) {
      builder.and(ordererIdEq(orderSearchCondition.getOrdererId()));
    }
    if (orderSearchCondition.getOrderState() != null) {
      builder.and(orderStateEq(orderSearchCondition.getOrderState()));
    }
    //builder 생성 완료

    List<OrderDto> result = queryFactory
        .select(Projections.constructor(OrderDto.class,
            order.orderNumber,
            order.orderState,
            order.shippingInfo,
            order.totalAmounts,
            order.orderer.name,
            order.createdAt
        ))
        .from(order)
        .where(builder)
        .fetch();

    return result;
  }
~~~

- BooleanBulider를 통해 builder를 생성하여 미리 where절에 들어갈 조건을 생성하였다
- 코드를 보면 기존의 where절에 조건을 넣는것보다 미리 빌더를 생성하니 더 깔끔해졌다.



#### 5.9 동적 인스턴스 생성

- JPA는 쿼리 결과 값을 임의의 객체를 동적으로 생성할 수 있다.

~~~java
@Query(value =
       //아래의 select 구문을 보면 new를 통해 dto 생성자를 호출한다.
      "select new com.example.ddd_start.order.domain.dto.OrderResponseDto(o, m, p) "
          + "from orders o join o.orderLines ol, Member m, Product p "
          + "where o.orderer.memberId = :memberId "
          + "and o.orderer.memberId = m.id "
          + "and ol.product_id = p.id")
  List<OrderResponseDto> findOrdersByMemberId(@Param("memberId") Long memberId);
~~~



- Querydsl은 쿼리 결과에서 임의의 객체를 동적으로 생성할 수 있다.

~~~java
 @Override
  public List<OrderDto> searchMyStateOrders(
      OrderSearchCondition orderSearchCondition) {
    BooleanBuilder builder = new BooleanBuilder();
    if (orderSearchCondition.getOrdererId() != null) {
      builder.and(ordererIdEq(orderSearchCondition.getOrdererId()));
    }
    if (orderSearchCondition.getOrderState() != null) {
      builder.and(orderStateEq(orderSearchCondition.getOrderState()));
    }

    List<OrderDto> result = queryFactory
      	//Proejctions.construector()를 통해 OrderDto의 생성자를 아래의 파라미터로 호출한다.
        .select(Projections.constructor(OrderDto.class,
            order.orderNumber,
            order.orderState,
            order.shippingInfo,
            order.totalAmounts,
            order.orderer.name,
            order.createdAt
        ))
        .from(order)
        .where(builder)
        .fetch();

    return result;
  }
~~~

- **JPQL 이든 Querydls이든 동적으로 인스턴스를 생성하면 가져갈 수 있는 이점은 조회전용 모델을 만들기 때문에 표현영역을 통해 사용자에게 적합한 데이터를 보여줄 수 있다.**

#### 5.10은 스킵



### 6. 응용 서비스와 표현 영역

#### 6.1 표현 영역과 응용 영역

- 도메인 영역을 잘 구현하지 않으면 사용자의 요구를 충족하는 제대로 된 소프트웨어를 만들지 못한다.
- 하지만 도메인 영역만 잘 만든다고 끝이 아니다. **도메인이 제 기능을 하려면 사용자와 도메인을 연결해주는 매개체가 필요하다.**
- 응용 영역과 표현 영역이 사용자와 도메인을 연결해주는 매개체 역할을 한다.

![ch6_1](./img/ch6_1.jpeg)

- **표현 영역은 사용자의 요청을 해석한다.**

  - 사용자가 웹 브라우저에서 폼에 ID와 암호를 입력한 뒤에 전송 버튼을 클릭하면 요청 파라미터를 포함한 HTTP요청을 표현 영역에 전달한다.
  - 요청 받은 표현 영역은 URL, 요청 파라미터, 쿠키, 헤더 등을 이용해서 사용자가 실행하고 싶은 기능을 판별하고 그 기능을 제공하는 응용서비스를 실행한다.

- **응용 영역의 서비스는 실제 사용자가 원하는 기능을 제공한다.**

  - 사용자가 회원 가입을 요청했다면 실제 그 요청을 위한 기능을 제공하는 주체는 응용 서비스에 위치한다.

  - 응용 서비스는 기능을 실행하는 데 필요한 입력 값을 메서드 인자로 받고 실행 결과를 리턴한다.

  - 응용 서비스의 메서드가 요구하는 파미터와 표현영익 사용자로부터 전달받은 데이터는 형식이 일치하지 않기 때문에 표현 영역은 응용 서비스가 요구 하는 형식으로 사용자 요청을 변환한다.

    - 예를 들면 표현영역의 코드는 다음과 같이 폼에 입력한 요청 파라미터값을 사용해서 응용 서비스가 요구하는 객체를 생성 한뒤, 응용 서비스의 메서드를 호출한다.

    ~~~java
    @PostMapping("/members/join")
      public ResponseEntity join(@RequestBody JoinMemberRequest req) {
        String email = req.getEmail();
        String password = req.getPassword();
        String name = req.getName();
        AddressRequest addressReq = req.getAddressReq();
    
        joinResponse joinResponse = memberService.joinMember(
            new joinRequest(email, password, name, addressReq));
    
        return new ResponseEntity<MemberResponse>(
            new MemberResponse(
                joinResponse.getMemberId(), joinResponse.getName(), "회원가입을 축하드립니다."),
            HttpStatus.ACCEPTED);
      }
    ~~~

    - 응용 서비스를 실행한 뒤에 표현영역은 실행결과를 사용자에게 알맞은 형식으로 응답한다.
      - ex) HTML, JSON

  - 사용자와 상호작용은 표현영역이 처리하기때문에, 응용서비스는 표현영역에 의존하지 않는다, 단지 기능 실행에 필요한 입력 값을 받고 실행결과만 리턴하면 된다.



#### 6.2 응용 서비스의 역할

- **응용 서비스는 사용자가 요청한 기능을 실행한다. 응용 서비스는 사용자의 요청을 처리하기 위해 레포지토리에서 도메인 객체를 가져와야 한다.**

- 응용 서비스의 주요 역할 은 도메인 객체를 사용해서 사용자의 요청을 처리하는 것이므로 표현 영역 입장에서 보았을 때 응용 서비스는 도메인 영역과 표현 영역을 연결해주는 창구 역할을 한다.

- 응용 서비스는 주로 도메인 객체 간의 흐름을 제어하기 때문에 단순한 형태를 갖는다.

  ~~~java
  public Result doSomeFunc(SomeReq req){
  	//1. 레포지토리에서 애그리거트를 구한다.
   	SomeAgg agg = someAggRepository.findById(req.getId());
   	checkNull(agg);
   	
   	//2. 애그리거트의 도메인 기능을 실행한다.
   	add.doFunc(req.getValue());
   	
   	//3. 결과를 리턴한다.
   	reutrn createSuccessResult(agg);
  }
  ~~~

- 새로운 애그리거트를 생성하는 응용 서비스 역시 간단하다.

  ~~~java
  public Result doSomeCreation(CreateSomeReq req){
  	//1. 데이터 중복 등 데이터가 유효한지 검사한다.
  	validate(req);
   	
   	//2. 애그리거트를 생성한다.
   	SomeAgg newAgg = createSome(req);
   	
   	//3. 레포지토리에서 애그리거트를 저장한다.
   	someAggRepository.save(newAgg);
   	
   	//4. 결과를 리턴한다.
   	reutrn createSuccessResult(agg);
  }
  ~~~

- 응용 서비스가 복잡하다면 응용서비스에서 도메인 로직의 일부를 구현하고 있을 가능성이 높다.

  - 응용 서비스가 도메인 로직을 일부 구현하면 코드 중복, 로직 분산등 코드 품질에 안좋은 영향을 줄 수 있다.

- 응용 서비스는 트랜잭션 처리도 담당한다. 응용 서비스는 도메인의 상태 변경을 트랜잭션으로 처리한다.

  ~~~java
  @Transactional
  public void blockMembers(Long[] blockingIds) {
     if (blockingIds == null | blockingIds.length == 0) {
       return;
     }
      
    List<Member> members = memberRepository.findByIdIn(blockingIds);
    members.forEach(
      Member::block
    );
  }
  ~~~

  - 상단의 메소드가 트랜잭션 범위에서 실행되지 않는다고 가정할 때, member 객체의 block() 메서드를 실행중에 문제가 발생하면 일부 Member만 차단되어, 데이터 일관성이 깨진다. 이런 상황이 발생하지 않으려면 트랜잭션 범위에서 롤백을 하여 전체 데이터가 아예 반영이 안되도록 하여 원자성을 지켜야 한다.

#### 6.2.1 도메인 로직 넣지 않기

- 도메인 로직은 도메인 영역에 위치하고 응용 서비스는 도메인 로직을 구현하지 않는다.

- 암호 변경 기능을 위한 응용 서비스느 Member 애그리거트와 관련된 레포지토리를 이용해서 도메인 객체간의 실행 흐름을 제어한다.

  ~~~java
  public class ChangePasswordService{
  	public void changePassword(Long memberId, String oldPw, String newPw){
  		Member member = memberRepository.findById(memberId);
  		checkMemberExists(member);
  		member.changePassword(oldPw, newPw)
  	}
  }
  ~~~

- Member 애그리거트는 암호를 변경하기전에 기존 암호를 올바르게 입력했는지 확인하는 로직을 구현한다.

  ~~~java
  {
  	if(this.pw.match(oldPw)) throw new BasPasswordException();
  }
  ~~~

- 기존 암호를 올바르게 입력했는지를  확인하는 것은 도메인의 핵심 로직이기 때문에 응용서비스에서 이 로직을 구현하면 안된다.
- 도메인 로직을 도메인 영역과 응용 서비스에서 분산해서 구현하면 코드 품질에 문제가 발생한다.
  - 문제점은 아래와 같다.
    - 코드의 응집성이 떨어진다. 
      - 도메인 데이터와 그 데이터를 조작하는 도메인 로직이 한 영역에 위치하지 않고 서로 다른 영역에 위치한다는 것은 도메인 로직을 파악하기 위해 여러 영역을 분석해야 한다는 것을 의미한다.
    - 여러 응용 서비스에서 동일한 도메인 로직을 구현할 가능성이 높아진다.
- 코드 중복을 막기 위해 응용 서비스 영ㅇ역에 별도의 보조 클래스를 만들 수 있지만, 애초에 도메인 영역에 암호 확인 기능을 구현했으면 응용 서비스는 그 기능을 사용만 하면 된다.
- 응용 서비스에서는 도메인이 제공하는 기능을 사용하면 응용 서비스가 도메인 로직을 구현하면서 발생하는 코드 중복 문제를 발생하지 않는다.
- 일부 도메인 로직이 응용 서비스에 출현하면서 발생하는 두가지 문제(응집도가 떨어지고, 코드 중복 발생)은 결과적으로 코드 변경을 어렵게 만든다.
  - 소프트웨어가 가져야할 중요한 경쟁 요소중 하나는 변경 용이성인데, 변경이 어렵다는 것은 그만큼 소프트웨어의 가치가 떨어진다는 것을 으미한다.
  - 소프트웨어의 가치를 높이려면 도메인 로직을 도메인 영역에 모아서 코드 중복을 줄이고 응집도를 높여야 한다.

#### 6.3 응용 서비스의 구현

- 응용 서비스는 표현 영역과 도메인 영역을 연결하는 매개체 역할을 하는데 이는 디자인 패턴에서 파사드와 같은 역할을 한다. 응용 서비스 자체는 복잡한 로직을 수행하지 않기 때문에 응용 서비스의 구현은 어렵지 않다. 

#### 6.3.1 응용 서비스의 크기

- 응용 서비스를 구현할 때, 응용 서비스의 크기를 고려해야 한다.

  - 회원 도메인을 예로 들 때, 회원 가입하기, 회원 탈퇴하기, 회원 암호 변경하기, 비밀번호 초기화하기와 같은 기능을 구현하기 위해 도메인 모델을 사용하게 된다.

  - 이 경우 응용 서비스는 다음 두가지 방법중 한가지 방식으로 구현한다.

    - **한 응용 서비스 클래스에 회원 도메인의 모든 기능 구현하기**
    - **구분되는 기능별로 응용 서비스 클래스를 따로 구현하기**

  - 회원과 관련된 기능을 한클래스에서 모두 구현하면 다음과 같은 모습을 갖는다. 각 메서드를 구현하는데 필요한 레포지토리나 도메인 서비스는 필드로 추가한다.

    ~~~java
    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class MemberService {
    
      private final MemberRepository memberRepository;
      private final PasswordEncryptionEngine passwordEncryptionEngine;
    
      @Transactional
      public joinResponse joinMember(joinRequest req) {...}
      @Transactional
      public void changePassword(Long id, String curPw, String newPw) {...}
      @Transactional
      public void initalizePassword(Long id) {...}
      @Transactional
      public void leave(Long id, String curPw) {...}
      ...
    }
    ~~~

  - 한 도메인과 관련된 기능을 구현한 코드가 한클래스에 위치하므로 각 기능에서 동일 로직에 대한 코드 중복을 제거할 수 있다는 장점이 있다.

    ~~~java
    private Notirifer notifier;
    @Transactional
    public void changePassword(Long id, String curPw, String newPw) {
      findExistMember(id);
      ...
    }
    @Transactional
    public void initalizePassword(Long id) {
      findExistMember(id);
      ...
      notifier.notifyNewPassword(member, newPassword);
    }
    
    
    private Member findExistMember(Long memberId){
    	Member member = memberRepository.findById(memberId);
    	if(member == null) throw new NoMemberException(memberId);
      return member;
    }
    ~~~

  - 위 코드와 같이 member가 존재하는지 확인하는 중복된 로직을 하나의 메서드로 쉽게 제거할 수 있다.

- 각 기능에서 동일한 로직을 위한 코드 중복을 제거하기 쉽다는 것이 장점이라면 한 서비스 클래스의 크기(코드 줄 수)가 커진다는 것은 이 방식의 단점이다.

- 코드 크기가 커지면 연관성이 적은 코드가 한 클래스에 함께 위치할 가능성이 높아지게 되는데 결과적으로 관련 없는 코드가 뒤섞여 코드를 이해하는데 방해가 된다.

  - 예를 들어 위코드에서 암호 초기화 기능을 구현한 initalizePassword() 메서드는 암호 초기화 후에 신규 암호를 사용자에게 통지하기 위해 Notifier를 사용하는데, 이 Notifier는 암호 변경 기능을 구현한 changePassword()에서는 필요하지 않는 기능이다.
  - 하지만 Notifier가 필드로 존재하기 때문에 이 Notifier가 어떤 기능 때문에 필요한지 확인하려면 각 기능을 구현한 코드를 뒤져야 한다.
  - 게다가 한 클래스에 코드가 모이기 시작하면 엄연히 분리하는 것이 좋은 상황임에도 습관적으로 기존에 존재하는 클래스에 억지로 끼워 넣게 된다. -> 이건은 코드를 점점 얽히게 만들어 코드 품질을 낮추는 결과를 초래한다.(스파게티 코드?)

- 구분되는 기능 별로 서비스 클래스를 구현하는 방식은 한 응용 서비스 클래스에서 한 개내지 2~3개의 기능을 구현한다.

  ~~~java
  public class ChangePasswordService {
  
    private MemberRepository memberRepository;
  
    public void changePassword(Long id, String curPw, String newPw) throws NoMemberFoundException {
      Member member = memberRepository.findById(id).orElseThrow(NoMemberFoundException::new);
      member.changePassword(curPw, newPw);
    }
  }
  ~~~

  - 이 방식을 사용하면 클래스 개수는 많아지지만 한 클래스에 관련 기능을 모두 구현하는 것과 비교해서 코드 품질을 일정 수준으로 유지하는 데 도움이 된다. 
  - 또한 각 클래스 별로 필요한 의존 객체만 포함하므로 다른 기능을 구현한 코드에 영향을 받지 않는다.

- 각 기능 마다 동일한 로직을 구현할 경우 하나의 Helper 또는 Support 클래스를 두어, 해당 클래스에서 공통 로직을 구현하게 하면 된다.

  ~~~java
  public class MemberServiceHelper{
  	public static Member findExistMember(MemberRepository memberRepository, Long memberId) {
      Member member = memberRepository.findById(memberId);
      if(member == null) throw new NoMemberException(memberId);
      return member;
  	}
  }
  ~~~

- 책의 필자는 한 클래스가 여러 역할을 갖는 것보다 각 클래스마다 구분되는 역할을 갖는것을 선호한다고 한다.

  - SOLID 의 SRP(단일 책임 원치)랑 비슷한것 같다. 하나의 클래스는 하나의 역할을 가져야 한다.

- 한 도메인과 관련된 기능을 하나의 응용 서비스 클래스에서 모두 구현하는 방식보다, 구분되는 기능을 별도의 서비스 클래스로 구현하는 방식을 사용한다.

#### 6.3.2 응용 서비스의 인터페이스와 클래스

- 응용 서비스를 구현할 때 인터페이스가 필요한지에 대해 논의 한다.
- 인터페이스가 필요한 몇가지 상황이 있다.
  - 구현 클래스가 여러개인 경우
    - 구현클래스가 다수 존재하거나 런타임에 구현 객체를 교체해야 할 때 인터페이스를 유용하게 사용할 수 있다.
    - 그런데 응용 서비스는 런타임에 교체하는 경우가 거의 없고 한 응용 서비스의 구현 클래스가 두개인 경우도 드물다.
    - 이렇듯 인터페이스와 클래스를 따로 구현하면 소스 파일만 많아지고 구현 클래스에 대한 간접 참조가 증가해서 전체 구조가 복잡해진다.
  - 테스트 주도 개발 (Test Driven Development)를 즐겨하고 표현 영역부터 개발을 시작한다면, 미리 응용 서비스를 구현할 수 없으므로 응용 서비스의 인터페이스부터 작성하게 될 것이다.
    - 미리 응용 서비스를 구현할 수 없으므로 응용 서비스의 인터페이스부터 작성하게 될 것이다.
  - 표현 영역이 아닌 도메인 영역이나 응용 영역의 개발을 먼저 시작하면 응용 서비스 클래스가 먼저 만들어진다.
    - 표현 영역의 단위 테스트를 진행할 때, 책이 이해가 안되지만 응용 서비스 객체를 생성해도 되고, 또는 Mock을 이용하여 테스트용 대역 객체를 만들 수 있다.
    - 이는 결과적으로 응용 서비스에 대한 인터페이스 필요성을 약화시킨다.



#### 6.3.3 메서드 파라미터와 값 리턴

- 응용 서비스가 제공하는 메서드는 도메인을 이용해서 사용자가 요구한 기능을 실행하는데 필요한 값을 파라미터로 전달받아야 한다.

  - 예를 들어 암호 변경 응용 서비스는 암호 변경 기능을 구현하는데 필요한 회원ID, 현재 암호, 변경할 암호를 파라미터로 전달받는다.

    ~~~java
    @Transactional
    //암호 변경 기능 구현에 필요한 값을 파라미터로 전달받음
    public void changePassword(Long id, String curPw, String newPw) {
      findExistMember(id);
      ...
    }
    ~~~

  - 위 코드처럼 필요한 각 값을 개별 파라미터로 전달 받을 수 있고, 다음 코드처럼 값 전달을 위한 별도 데이터 클래스를 만들어 전달 받을 수 있다.

    ~~~java
    @Getter
    @RequiredArgsConstructor
    public class ChangePasswordRequest {
    
      private final Long memberId;
      private final String curPw;
      private final String newPw;
    }
    ~~~

  - 응용 서비스는 파라미터로 전달 받은 데이터를 사용해서 필요한 기능을 구현하면 된다.

    ~~~java
    public void changePassword(ChangePasswordRequest req) throws NoMemberFoundException {
        Member member = memberRepository.findById(req.getMemberId())
            .orElseThrow(NoMemberFoundException::new);
        member.changePassword(req.getCurPw(), req.getNewPw());
    }
    ~~~

  - 스프링 MVC와 같은 웹 프레임워크는 웹 요청 파라미터를 자바 객체로 변환하는 기능을 제공하므로 응용 서비스에 데이터로 전달할 요청 파라미터가 두 개 이상 존재하면 데이터 전달을 위한 별도 클래스를 사용하는 것이 편리하다.

    ~~~java
    @GetMapping("/members/change-password")
    public ResponseEntity changePassword(ChangePasswordRequest req) throws NoMemberFoundException {
      changePasswordService.changePassword(
        new ChangePasswordCommand(
          req.getMemberId(),
          req.getCurPw(),
          req.getNewPw()));
    
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    ~~~

  - 응용 서비스의 결과를 표현 영역에서 사용해야 하면 응용 서비스 메서드의 결과로 필요한 데이터를 리턴한다. 

  - 결과 데이터가 필요한 대표적인 예가 식별자다.

    - 온라인 쇼핑몰은 주문 후 주문 상세 내역을 볼 수 있는 링크를 바로 보여준다. 이링크를 제공하려면 방금 요청한 주문의 번호를 알아야 한다. 이 요구를 충족하려면 주문 응용 서비스는 주문 요청 처리 후에 주문번호를 결과로 리턴해야 한다.

      ~~~java
      @Transactional
      public Long placeOrder(PlaceOrderCommand placeOrderCommand) {
        Order order = new Order(placeOrderCommand.getOrderLines(), placeOrderCommand.getShippingInfo(),
        placeOrderCommand.getOrderer());
        orderRepository.save(order);
          //응용 서비스 실행 후 표현 영역에서 필요한 값 리턴
        return order.getId();
      }
      ~~~

    - 위 코르를 사용하는 표현 영역 코드는 응용 서비스가 리턴한 값을 사용해서 사용자에게 알맞은 결과를 보여줄 수 있게 된다.

  - 응용 서비스에서 애그리거트 객체를 그대로 리턴할 수 있다. 허나 나는 비추한다.

    - 왜냐하면 표현영역에서 도메인에 대한 의존이 생겨버려 코드의 품질이 떨어진다.

  - 응용서비스에서 애그리거트 자체를 리턴하면 코딩은 편할 수 있지만, 도메인 로직 실행을 응용서비스와 표현 영역 두 곳에서할 수 있게 된다. 이것은 기능 실행 로직을 응용 서비스와 표현 영역에 분산시켜 코드의 응집도를 낮추는 원인이 된다.

- **응용 서비스는 표현 영역에서 필요한 데이터만 리턴하는 것이 기능 실행 로직의 응집도를 높이는 확실한 방법이다**.



#### 6.3.4 표현 영역에 의존하지 않기

- **응용 서비스 파라머티 타입을 결정할 때 주의할 점은 표현 영역과 관련된 타입을 사용하면 안된다.**

  - 예를 들어 표현영역의 HttpServletRequest나 Session을 응용 서비스 파라미터로 전달하면 안도니다.

  ~~~java
  @PostMapping
  public String submti(HttpServletRequest req){
  	//절대 응용 서비스에 파라미터로 넘기면 안된다!
  	changePasswordService.changPassword(request);
  }
  ~~~

- 응용 서비스에서 표현 영역에 대한 의존이 발생하면 응용 서비스 단독으로 테스트하기가 어려워 진다.

  - 게다가 표현 영역의 구현이 변경되면 응용 서비스의 구현도 함께 변경해야 하는 문제도 발생한다.

- 위의 문제 보다 더 심각한 것은 응용 서비스가 표현 영역의 역할까지 대신하는 상황이 벌어질 수 있다.

  - 예를 들어 응용 서비스에 파라미터로 HttpServletRequest를 전달했는데 응용 서비스에서 HttpSession을 생성하고 세션에 인증과 관련된 정보를 담는다고 해보자

    ~~~java
    public void authenticate(HttpServletRequest request) {
     	String id = request.getParmater("id");
     	String password = request.getPassword("password");
     	if(checkIdPasswordMatching(id, password)) {
     		// 응용 서비스에서 표현 영역의 상태 처리
     		HttpSession session = request.getSession();
     		session.setAttribute("auth", new Authentication(id));
     	}
    }
    ~~~

  - HttpSession이나 쿠키는 표현 영역의 상태에 해당하는데 이 상태를 응용 서비스에서 변경해버리면 표현 영역의 코드만으로 표현 영역의 상태가 어떻게 변경되는지 추적하기 어렵다.
  - 즉 표현 영역의 응집도가 깨지는 것이다. 이것은 결과적으로 코드 유지보수 비용을 늘리고, 품질을 떨어뜨린다.

- 문제가 발생하지 않도록 하려면 철저하게 응용 서비스가 표현 영역의 기술을 사용하지 않도록 해야한다. 이를 지키기 위한 가장 쉬운 방법은 서비스 메서드의 파라미터와 리턴타입으로 표현 영역의 구현 기술을 사용하지 않는 것이다.



#### 6.3.5 트랜잭션 처리

- 어떤 기능이 실행되고 화면에는 기능이 성공했다고 하더라도, 해당 기능에 대한 변수들이 DB에 반영이 되지 않으면 추후에 해당 값을 참조할 때, 기능이 정상적으로 동작하지 않는다.

  - 위와 트랜잭션과 관련된 문제는 트랜잭션을 관리한느 응용 서비스의 중요한 역할이다.

    > 트랜잭션이란: 작업의 단위 이다. 성질로는 ACID가 있다.

- 스프링과 같은 프레임워크가 제공하는 트랜잭션 관리기능을 이용하면 쉽게 트랜잭션을 처리할 수 있다.

  ~~~java
  @Transactional
  public void changePassword(ChangePasswordRequest req){
  	Member member = memberRepository.findById(req.getMemberId())
          .orElseThrow(NoMemberFoundException::new);
  	member.changePassword(req.getCurPw(), req.getNewPw());
  }
  ~~~

- 프레임워크가 제공하는 트랜잭션 기능을 적극 사용하는 것이 좋다. 프레임워크가 제공하는 규칙을 따르면 간단한 설정만으로 트랜잭션을 시작하여 커밋하고 익셉션이 발생하면 롤백 할 수 있다. 

  >  (커밋과 롤백은 ACID의 A인 Atomic(원자성)과 관련되어 있으며, 트랜잭션은 모두 반영되거나, 작업중 하나라도 실패하면 모두 롤백되어야 한다.)

- 스프링은 @Transactional이 적용된 메서드가 RuntimeException을 발생시키면 트랜잭션을 롤백하고 그렇지 않으면 커밋하므로, 해당 규칙에 따라 트랜잭션 처리 코드를 간결하게 유지할 수 있다.



#### 6.4 표현 영역

- 표현영역의 책임은 크게 다음과 같다.

  - 사용자가 시스템을 사용할 수 있는 흐름(화면)을 제공하고 제어한다.
  - 사용자의 요청을 알맞은 응용 서비스에 전달하고 결과를 사용자에게 제공한다.
  - 사용자의 세션을 관리한다.

- 표현 영역의 첫번 째 책임은 사용자가 시스템을 사용할 수 있도록 알맞은 흐름을 제공하는 것이다.

  - 웹서비스의 표현 영역은 사용자가 요청한 내용을 응답으로 제공한다. 응답에는 다음화면으로 이동할 수 있는 링크나 데이터를 입력하는데 필요한 폼등이 포함된다.

    <img src="./img/presentation1.JPG" style="zoom:33%;" />

  - 사용자는 표현 영역이 제공한 폼에 알맞은 값을 입력하고, 다시 폼을 표현 영역에 전송한다. 표현 영역은 응용 서비스를 이용해서 표현 영역의 요청을 처리하고 그결과를 응답으로 전송한다.

- 표현 영역의 두 번째 책임은 사용자의 요청에 맞게 응용 서비스에 기능 실행을 요청하는 것이다. 화면을 보여주는데 필요한 데이터를 읽거나 도메인의 상태를 변경해야할 때 응용서비스를 사용한다.

  - 이 과정에서 표현 영역은 사용자의 요청 데이터를 응용 서비스가 요구하는 형식으로 변환하고 응용 서비스의 결과를 사용자에게 응답할 수 있는 형식으로 변환한다.

  - 예를 들어 암호 변경을 처리하는 표현 영역은 다음과 같이 HTTP 요청 파라미터로부터 필요한 값을 읽어와 응용 서비스의 메서드가 요구하는 객체로 변환해서 요청을 전달한다.

    ~~~java
    @GetMapping("/members/change-password")
    public ResponseEntity changePassword(ChangePasswordRequest req) throws NoMemberFoundException {
        changePasswordService.changePassword(
          //응용 서비스가 요구하는 형식으로 변환하여 파라미터를 넘겨준다.
            new ChangePasswordCommand(
                req.getMemberId(),
                req.getCurPw(),
                req.getNewPw()));
    
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    ~~~

  - MVC 프레임워크는 HTTP 요청파라미터로부터 자바 객체를 생성하는 기능을 지원하므로, 위와 같이 객체로 받으면 프레임워크가 알아서 해당 객체에 맞는 값을 주입해준다.
  - 책에서는 표현영역의 파라미터를 응용서비스에 바로 전달을 해줬으나, 나는 응용서비스에 따로 요청 파라미터를 두었다.

- 응용 서비스의 실행결과를 사용자에게 알맞은 형식으로 제공하는 것도 표현 영역의 몫이다.

  - 응용 서비스에서 익셉션이 발생하면 에러코드를 설정하는데 표현 영역의 뷰는 이 에러코드에 알맞는 처리(해당하는 메시지 출력과 같은)를 하게 된다.
  - 표현 영역의 다른 주된 역할은 사용자의 연결 상태인 세션을 관리하는 것이다. 웹은 쿠키나 서버 세션을 잉요해서 사용자의 연결 상태를 관리한다.

#### 6.5 값 검증

- 값 검증은 표현 영역과 응용 서비스 두 곳에서 모두 수행할 수 있다. 원칙적으로 모든 값에 대한 검증은 응용 서비스에서 처리한다. 

  - 예를 들어 회원 가입을 처리하는 응용 서비스는 파라미터로 전달받은 값이 올바른지 검사해야한다.

  ~~~java
  @Service
  @RequiredArgsConstructor
  public class JoinMemberService {
  
    private final MemberRepository memberRepository;
    private final PasswordEncryptionEngine passwordEncryptionEngine;
  
    @Transactional
    public joinResponse joinMember(joinCommand req) throws DuplicateEmailException {
      //값의 형식 검사
      checkEmpty(req.getEmail(), "email");
      checkEmpty(req.getPassword(), "password");
      checkEmpty(req.getName(), "name");
  
      //로직 검사
      checkDuplicatedEmail(req.getEmail());
  
      AddressCommand addressReq = req.getAddressReq();
      Address address = new Address(addressReq.getCity(), addressReq.getGuGun(), addressReq.getDong(),
          addressReq.getBunji());
  
      String encryptedPassword = passwordEncryptionEngine.encryptKey(req.getPassword());
      Member member = new Member(req.getName(), req.getEmail(), new Password(encryptedPassword),
          address);
  
      memberRepository.save(member);
  
      return new joinResponse(member.getId(), member.getName());
    }
  
    private void checkDuplicatedEmail(String email) throws DuplicateEmailException {
      Long count = memberRepository.countByEmail(email);
      if (count > 0) {
        throw new DuplicateEmailException();
      }
    }
  
    private void checkEmpty(String value, String propertyName) {
      if (value == null || value.isEmpty()) {
        throw new NullPointerException(String.format("{}는 빈 값입니다.", propertyName));
      }
    }
  }
  ~~~

  - 표현 영역은 잘못된 값이 존재하면 이를 사용자에게 알려주고 값을 다시 입력받아야 한다. 

  - Spring MVC는 폼에 입력한 값이 잘못된 경우 에러메시지를 보여주기 위한 용도로 Errors나 BindingReulst를 사용하는데, 컨트롤러에서 위와 같은 응용 서비스를 사용하면 폼에 에러 메시지를 보여주기 위해 다음과 같이 다소 번잡한 코드를 사용한다.

    ~~~java
    @PostMapping("/members/join")
      public ResponseEntity join(@RequestBody JoinMemberRequest req, Errors errors){
        String email = req.getEmail();
        String password = req.getPassword();
        String name = req.getName();
        AddressCommand addressReq = req.getAddressReq();
    
        try {
          joinResponse joinResponse = joinMemberService.joinMember(
              new joinCommand(email, password, name, addressReq));
    
          return new ResponseEntity<MemberResponse>(
              new MemberResponse(
                  joinResponse.getMemberId(), joinResponse.getName(), "회원가입을 축하드립니다."),
              HttpStatus.ACCEPTED);
        } catch (DuplicateEmailException e) {
          errors.rejectValue(e.getMessage(), "duplicate");
          return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (...) {
    			...    
        }
      }
    ~~~

  - 응용 서비스에서 각 값이 유효한지 확인할 목적으로 익셉션을 사용할 때의 문제점은 사용자에게 좋지 않은 경험을 제공한다는 것이다. 

  - 사용자는 폼에 값을 입력하고 전송했는데 입력한 값이 잘못되어 다시 폼에 입력해야 할 때 한 개 항목이 아닌 입력한 모든 항목에 대해 잘못된 값이 존재하는지 알고 싶을 것이다. 그래야 한번에 잘못된 값을 제대로 입력할 수 있기 때문이다.

  - 그런데 응용서비스에서 값을 검사하는 시점에 첫 번째 값이 올바르지 않아 익셉션을 발생시키면 나머지 항목에 대해서는 값을 검사하지 않게 된다. 

    - ​	이러면 사용자는 첫 번째 값에 대한 에러메시지만 보게 되고 나머지 항목에 대해서는 값이 올바른지 알 수 없게 된다. 이는 사용자가 같은 폼에 값을 여러번 입력하게 만든다.

  - 이런 사용자 불편을 해소하기 위해 응용 서비스에서 에러코드를 모아 하나의 익셉션으로 발생시키는 방법이 있다.

  - 아래의 코드는 값 검증 시, 잘못된 값이 존재하면 errors에 추가를 한다. 값 검증이 끝난 뒤에 errors에 값이 존재하면 erros 목록을 갖는 ValidationErrorExceptino을 발생시켜 입력 파라미터 값이 유효하지 않다는 사실을 알린다.

    ~~~java
    @Transactional
      public Long placeOrderV2(PlaceOrderCommand command) throws ValidationErrorException {
        List<ValidationError> errors = new ArrayList<>();
    
        if (command == null) {
          errors.add(ValidationError.of("empty"));
        } else {
          if (command.getOrderer() == null) {
            errors.add(ValidationError.of("orderer", "empty"));
          }
          if (command.getOrderLines() == null) {
            errors.add(ValidationError.of("orderLine", "empty"));
          }
          if (command.getShippingInfo() == null) {
            errors.add(ValidationError.of("shippingInfo", "empty"));
          }
        }
    
        if (!errors.isEmpty()) {
          throw new ValidationErrorException(errors);
        }
    
        Order order = new Order(command.getOrderLines(), command.getShippingInfo(),
            command.getOrderer());
        orderRepository.save(order);
        return order.getId();
      }
    ~~~

  - 표현 영역은 응용 서비스가 ValidationErrorException을 발생시키면 다음 코드처럼 익셉션에서 에러 목록을 가져와 표현 영역에서 사용할 형태로 변환 처리한다.

    ~~~java
    @PostMapping("/orders/place-order")
    public Long order(@RequestBody PlaceOrderRequest req, BindingResult bindingResult) {
      try {
        Long orderId = orderService.placeOrderV2(
          new PlaceOrderCommand(req.getOrderLines(), req.getShippingInfo(), req.getOrderer()));
    
        return orderId;
      } catch (ValidationErrorException e) {
        e.getErrors().forEach(err -> {
          if (err.hasName()) {
            bindingResult.rejectValue(err.getPropertyName(), err.getValue());
          } else {
            bindingResult.reject(err.getValue());
          } 
        });
    
        throw new RuntimeException(e);
      }
    }
    ~~~

  - 표현 영역에서 필수 값을 검증하는 방법도 있다.

  - 스프링과 같은 프레임워크는 값 검증을 위한 Validator 인터페이스를 별도로 제공하므로 이 인터페이스를 구현한 검증기를 따로 구현하면 간결하게 구현할 수 있다.

    ~~~
    @PostMapping("/member/join")
    public String join(joinRequest joinRequest, Errors erros) {
    	JoinRequestValidator().validate(joinRequest, errors);
    	if(errors.hasErrors()) return formView;
    	
    	try{
        joinService.join(joinRequest);
        return successView;
    	} catch(DuplicateIdException ex) {
    		errors.rejectValue(ex.getPropertyName(), "duplicate");
    		return formView;
    	}
    }
    ~~~

  - 이렇게 표현 영역에서 필수 값과 값의 형식을 검사하면 실질적으로 응용 서비스는 ID 중복 여부와 같은 논리적 오류만 검사하면 된다.

  -  즉 표현 영역과 응용 서비스가 값 검사를 나눠서 수행하는 것이다. 응용 서비스를 사용하는 표현 영역 코드가 한 곳이면 구현의 편리함을 위해 다음과 같이 역할을 나눌 수 있다.

    - 표현 영역: 필수 값, 값의 형식, 범위 등을 검증한다.
    - 응용 서비스: 데이터의 존재 유무와 같은 논리적 오류를 검증한다.

  - 응용 서비스에서 얼마나 엄격하게 값을 검증해야 하는지에 대허슨 의견이 갈릴 수 있다.

    - 책의 필자는 최근에 응용 서비스에서 필수값 검증과 논리적인 검증을 모두 하는 편이라한다.

    - 응용 서비스에서 필요한 값 검증을 모두 처리하면 프레임워크가 제공하는 검증 기능을 사용할 때 보다 작성할 코드가 늘어나는 불편함이 있지만 반대로 응용 서비스의 완성도가 높아지는 이점이 있다. 필자는 이런 이점이 더크게 느껴져 응용 서비스에서 값 오류를 검증하는 편이라 한다.

      > 질문 - 그렇다면 @Valid(NonNull, NonBlak와 같은것들도 포함)는 언제쓰는게 좋을까?

  

#### 6.6 권한 검사

- 권한이란 사용자가 특정 기능 사용 또는 자원 접근에 대하여 권한을받을 수 있다.

  - 예를 들면 '상점주는 상품 등록이 가능하지만, 고객은 상품 등록이 불가능 하다. 또는 자신의 개인정보는 볼 수 있지만, 남의 개인정보는 볼 수 없다.'

- 개발하는 시스템마다 권한의 복잡도가 다르다. 단순한 시스템은 인증 여부만 검사하면 되는데 반해, 어떤 시스템은 관리자인지에 따라 사용할 수 있는 기능이 달라진다.

- 또는 실행할 수 있는 기능이 역할마다 달라지는 경우도 있다.

  - 이런 다양한 상황을 충족하기 위해 스프링 시큐리티(Spring Security) 같은 프레임워크는 유연하고 확장 가능한 구조를 갖고 있다.
  - 이는 유연한 만큼 복잡하다는 것을 의미한다.
  - 보안 프레임워크에 대한 이해가 부족하면 프레임워크를 무턱대고 도입하는 것보다 개발할 시스템에 맞는 권한 검사 기능을 구현하는 것이 시스템 유지 보수에 유리할 수 있다.

- 보안 프레임워크의 복잡도를 떠나 보통 다음 세곳에서 권한 검사를 수행할 수 있다.

  - 표현 영역
  - 응용 서비스
  - 도메인

- 표현 영역에서 할 수 있는 기본적인 검사는 인증된 사용자인지 아닌지 검사하는 것이다. 

  - 대표적인 예가 회원 정보 변경기능이다. 회원 정보 변경과 관련된 URL은 인증된 사용자마 접근해야 한다. 회원 정보 변경을 처리하는 URL에 대해 표현 영역은 다음과 같이 접근 제어를 할 수 있다.

    - URL을 처리하는 컨트롤러에 웹 요청을 전달하기 전에 인증 여부를 검사해서 인증된 사용자의 웹 요청만 컨트롤러에 전달한다.
    - 인증된 사용자가 아닐 경우 로그인 화면으로 리다이렉트Redirect 시킨다.

  - 이런 접근 제어를 하기에 좋은 위치가 서블릿 필터(또는 인터셉터?)이다.

  - 서블릿 필터에서 사용자의 인증정보를 생성하고 인증 여부를 검사한다. 인증된 사용자면 다음 과정을 진행하고 그렇지 않으면 로그인 화면이나 에러 화면을 보여주면 된다.

    <img src="./img/presentation2.JPEG" style="zoom:33%;" />

  - 인증 여부뿐만 아니라 권한에 대해서 동일한 방식으로 필터를 사용해서 URL별 권한 검사를 할 수 있다. 스프링 시큐리티는 이와 유사항 방식으로 필터를 이용해서 인증 정보를 생성하고 웹 접근을 제어한다.

- URL 만으로 접근 제어를 할 수 없는 경우 응용 서비스의 메서드 단위로 권한 검사를 수행해야 한다. 이것이 꼭 응용 서비스의 코드에서 직접 권한 검사를 해야 한다는 것을 의미하는 것은 아니다.

  - 예를 들어 스프링 시큐리티는 AOP를 활용해서 다음과 같이 애너테이션으로 서비스 메서드에 대한 권한 검사를 할 수 있는 기능을 제공한다.

    ~~~java
    public class BlockMemberService {
    
      private MemberRepository memberRepository;
      
      @PreAuthorize("hasRole('ADMIN')")
      public void block(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        member.block();
      }
    }
    ~~~

  - 개별 도메인 객체 단위로 권한 검사를 해야 하는 경우는 구현이 복잡해진다. 

  - 예를 들어 게시글 삭제는 본인 또는 관리자 역할을 가진 사용자만 할 수 있다 할 때, 게시글 작성자가 본인인지 확인하려면 게시글 애그리거트를 먼저 로딩해야 한다. 즉 응용 서비스의 메서드 수준에서 권한 검사를 할 수 없기 때문에(질문 - 이게 무슨말인지 이해가 안간다? 그렇다면 도메인 서비스에서 해야 되는 것인가) 다음과 같이 직접 권한 검사 로직을 구현해야 한다.

    ~~~java
    public class DeleteArticleService {
    	public void delete(Long userId, Long articleId) {
    		Article article = articleRepository.findByID(articleId);
    		checkArticleExistence(article);
    		permissionService.checkDeletePermission(userId, article);
    		article.markDeleted();
    	}
    }
    ~~~

  - permissionService.checkDeletePermission()은 파라미터로 전달받은 사용자 ID와 게시글을 이용해서 삭제 권한을 가졌는지를 검사한다.

- 스프링 시큐리티와 같은 보안 프레임워크를 확장해서 개별 도메인 객체 수준의 권한 검사 기능을 프레임워크에 통합할 수 있다. 

- 도메인 객체 수준의 권한 검사 로직은 도메인별로 다르므로 도메인에 맞게 보안 프레임워크를 확장하려면 프레임워크에 대한 높은 이해가 필요하다.

- 이해도가높지 않아 프레임워크 확장을 원하는 수준으로 할 수 없다면 프레임워크를 사용하는 대신 도메인에 맞는 권한 검사 기능을 직접 구현하는 것이 코드 유지보수에 유리하다.



#### 6.7 조회 전용 기능과 응용 서비스

- 서비스에서 조회 전용 기능을 사용하면 서비스 코드가 다음과 같이 단순히 조회 전용 기능을 호출하는 형태로 끝날 수 있다.

  ~~~java
  public class OrderListService {
  	public List<OrderView> getOrderList(Long ordererId) {
  		return orderViewDao.selectByOrderer(ordererId);
  	}
  }
  ~~~

- 서비스에서 수행하는 추가적인 로직이 없을 뿐더러 단일 쿼리만 실행하는 조회 전용 기능이여서 트랜잭션은 readOnly = true로만 조회 하면된다.(질문 - 책에서는 트랜잭션이 필요하지 않다고 한다. 이해가 안간다.)

- 책에서는 이 경우라면 굳이 서비스를 만들 필요없이 표현 영역에서 바로 조회 전용 기능을 사용해도 문제가 없다고 한다.

  ~~~
  public class OrderController {
  	private OrderViewDao orderViewDao
  	
  	@RequestMapping("/myorders")
  	public String list(ModelMap model) {
  		Long ordererId = SecurityContext.getAuthentication().getId();
  		List<OrderView> orders = orderViewDao.selectByOrderer(ordererId);
  		model.addAttribute("orders", orders);
  		return "order/list"
  	}
  }
  ~~~

- 응용 서비스를 항상 만들었던 개발자는 컨트롤러와 같은 표현 영역에서 응용 서비스 없이 조회 전용 기능에 접근하는것이 이상하게 느껴질 수 있다.

  - 하지만 응용 서비스가 사용자 요청 기능을 실행하는 데 별 다른 기여를 하지 못한다면 굳이 서비스를 만들지 않아도 된다.

    > 허나 나는 그래도 서비스를 만들겠다 왜냐하면 표현 영역이 도메인에 의존하기 때문에 코드 품질이 떨어지고 유지보수성이 불편하기 때문에 조회 전용에 대해서도 결국엔 하나가 아니라 여러개가 될 수 있으니까 응집성이 높아지게 하나로 모으는게 더 좋을 것 같다.

<img src="./img/presentation3.JPG" alt="presentation3" style="zoom:33%;" />

