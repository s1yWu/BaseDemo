package demo.factory

/**
 * Created by s1y on 2019/3/15.
 */
class CarFactory : Factory {
    override fun produce(): Product {
        return FactoryCar()
    }
}