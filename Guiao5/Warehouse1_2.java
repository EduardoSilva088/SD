package Guiao5;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Warehouse1_2 {
    private Map<String, Product> m = new HashMap<String, Product>();

    private Lock l = new ReentrantLock();
    

    private class Product {
        int q = 0;
        Condition c = l.newCondition();
    }

    private Product get(String s) {
        Product p = m.get(s);
        if (p != null)
            return p;
        p = new Product();
        m.put(s, p);
        return p;
    }

    public void supply(String s, int q) {
        l.lock();
        try{
            Product p = get(s);
            p.q += q;
            p.c.signalAll();
        } finally{
            l.unlock();
        }
    }


  // Devolve algum produto que esteja sem stock
  private Product missing(String[] s){
    for(String a: s){
      Product p = get(a);
      if(p.q==0){
        return p;
      }
    }
    return null;
  }

// Reduz o stock a cada produto
  private Product consumeEach(String[] s){
    for(String a: s){
      get(a).q--;
    }
    return null;
  }

  private void consume(String[] a) throws InterruptedException{
    try {
      wharehouseLock.lock();


      // Bloco que verifica se há algum pedido em falta a consumir
      // Caso estejam todos disponíveis o ciclo é quebrado  
      while(true){ 
        Product p2 = missing(a);
        if(p2==null){
          break;
        }
        p2.productCondition.await();
      }

      consumeEach(a);
    }finally {
      wharehouseLock.unlock();
    }
  }
}
