package model00.aliasdemo;

import javax.persistence.*;

@Entity
public class EntityC {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne
  private EntityD entityD;
}
