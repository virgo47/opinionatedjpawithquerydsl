package model00.aliasdemo;

import javax.persistence.*;

@Entity
public class EntityA {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne
  private EntityB entityB;
}
