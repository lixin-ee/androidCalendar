package com.example.myapp.entity;

public interface TSitem {
    public final int TASK=1;
    public final int SCHEDULE=2;
  public String getDescription();
  public String getTime1();
  public String getTime2();
  public String getType();
  public int getOverDue();
  public int getTStype();
  public int getTxing();
  public int getId();
}
