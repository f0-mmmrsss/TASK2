package task2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class t1 {
    public abstract class Drinks {
        protected String name;
        protected double cost;
        protected LocalDate DateOfManufacture;
        protected int num;
        public Drinks(){}
        public Drinks(String name, double cost, LocalDate DateOfManufacture, int num) {
            this();
            this.name = name;
            this.cost = cost;
            this.DateOfManufacture = DateOfManufacture;
            this.num = num;
        }

        public void stock(int number) {
            num += number;
        }

        public String getname() {
            return name;
        }

        public int getNum() {
            return num;
        }

        public void useDrinks() {
            this.num--;
        }
        public void Overdue(){
            this.num = 0;
        }
        public abstract String toString();
    }

    public class Beer extends Drinks {
        private float degrees;
        private int expiration;
        public Beer(String name, double cost, LocalDate DateOfManufacture, int num, float degrees) {
            super(name, cost, DateOfManufacture, num);
            this.expiration = 30;
            this.degrees = degrees;
        }
        public int getExpiration() {
            return expiration;
        }
        public boolean IsPastDue(LocalDate date) {
            return DateOfManufacture.plusDays(this.expiration).isAfter(date);
        }
        private void use(Beer p_beer,LocalDate date, ArrayList<Beer> beer_Gather) throws IngredientSortOutException {
            boolean flag = true;
            for (Beer beer : beer_Gather) {
                if (beer.getname().compareTo(p_beer.getname()) == 0) {
                    flag = false;
                    try {
                        if (beer.IsPastDue(date)) beer.Overdue();
                        else if (beer.getNum() > 0) {
                            beer.useDrinks();
                        } else {
                            throw new IngredientSortOutException();
                        }
                    } catch (IngredientSortOutException e) {
                        System.out.println(beer.getname());
                    }
                    return;
                }
            }
            if (flag) {
                System.out.println("该种啤酒库存中不存在，请更改套餐！");
            }
        }
        public String toString() {
            return "name is " + name +
                    ", cost is " + cost +
                    ", LocalDate is " + DateOfManufacture +
                    ", num is" + num +
                    ", degrees is" + degrees +
                    ", expiration is" + expiration;
        }

    }

    public class Juice extends Drinks {
        private int expiration;

        public Juice(String name, double cost, LocalDate DateOfManufacture, int num) {
            super(name, cost, DateOfManufacture, num);
            this.expiration = 2;
        }

        public int getExpiration() {
            return expiration;
        }

        public boolean IsPastDue(LocalDate date) {
            return DateOfManufacture.plusDays(this.expiration).isAfter(date);
        }

        public String toString() {
            return "name is " + name +
                    ", cost is " + cost +
                    ", LocalDate is " + DateOfManufacture +
                    ", num is" + num +
                    ", expiration is" + expiration;
        }
        private void use(Juice p_juice, LocalDate date, ArrayList<Juice> juice_Gather) throws IngredientSortOutException {
            boolean flag = true;
            for (Juice juice : juice_Gather) {
                if (juice.getname().compareTo(p_juice.getname()) == 0) {
                    flag = false;
                    try {
                        if (juice.IsPastDue(date)) juice.Overdue();
                        else if (juice.getNum() > 0) {
                            juice.useDrinks();
                        } else {
                            throw new IngredientSortOutException();
                        }
                    } catch (IngredientSortOutException e) {
                        System.out.println(juice.getname());
                    }
                    return;
                }
            }
            if (flag) {
                System.out.println("该种果汁库存中不存在，请更改套餐！");
            }
        }
    }

    public class SetMeal extends Drinks {
        private String name_SetMeal;
        private double cost_SetMeal;
        private String name_Fried;
        public Drinks SetMeal_drink;

        public SetMeal(String name_SetMeal, double cost_SetMeal, String name_Fried, Beer beer) {
            this.name_SetMeal = name_SetMeal;
            this.cost_SetMeal = cost_SetMeal;
            this.name_Fried = name_Fried;
            this.SetMeal_drink = new Beer(beer.name, beer.cost, beer.DateOfManufacture, beer.num, beer.degrees);
        }
        public SetMeal(String name_SetMeal, double cost_SetMeal, String name_Fried, Juice juice) {
            this.name_SetMeal = name_SetMeal;
            this.cost_SetMeal = cost_SetMeal;
            this.name_Fried = name_Fried;
            this.SetMeal_drink = new Juice(juice.name, juice.cost, juice.DateOfManufacture, juice.num);
        }

        public String toString() {
            return "SetMeal is" + name_Fried +
                    ",cost is " + cost_SetMeal +
                    ",Fried name is " + name_Fried +
                    ",drink is " + SetMeal_drink;
        }
    }

    interface FriedChickenRestaurant {
        public abstract void sell_SetMeal(SetMeal p_meal, LocalDate date);

        public abstract void batch_Stock(Drinks drinks);
    }

    public class IngredientSortOutException extends RuntimeException {
        public IngredientSortOutException() {
            System.out.print("已经耗尽");
        }
    }

    public class OverdraftBalanceException extends RuntimeException {
        //double用来储存当出现异常所缺乏的金额
        private double amount;

        public OverdraftBalanceException(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }
    }

    public class West2FriedChickenRestaurant implements FriedChickenRestaurant {
        public double balance;
        public ArrayList<SetMeal> meal_Gather = new ArrayList<SetMeal>();
        public ArrayList<Beer> beer_Gather;
        public ArrayList<Juice> juice_Gather;

        public void sell_SetMeal(SetMeal p_meal, LocalDate date) {
            boolean flag = true;
            for (SetMeal meal : meal_Gather) {
                if (meal.getname().compareTo(p_meal.getname()) == 0) {
                    flag = false;
                    if (meal.SetMeal_drink instanceof Beer)
                        ((Beer) meal.SetMeal_drink).use((Beer) meal.SetMeal_drink, date, beer_Gather);
                    if (meal.SetMeal_drink instanceof Juice)
                        ((Juice) meal.SetMeal_drink).use((Juice) meal.SetMeal_drink, date, juice_Gather);
                    return;
                }
            }
            if (flag) {
                System.out.println("该套餐不存在！");
            }
        }

        public void batch_Stock(Drinks drinks) throws OverdraftBalanceException {
            double amount = drinks.cost * drinks.getNum();
            try {
                if (amount <= balance) {
                    balance -= amount;
                } else {
                    double needs = amount - balance;
                    throw new OverdraftBalanceException(needs);
                }
            } catch (OverdraftBalanceException e) {
                System.out.println("余额不足！ 差值为 $" + e.getAmount());
                return;
            }
            if (drinks instanceof Beer) {
                for (Beer beer : beer_Gather) {
                    if (beer.getname().compareTo(drinks.getname()) == 0) {
                        beer.stock(drinks.getNum());
                        return;
                    }
                }
                Beer beer = (Beer) drinks;
                beer_Gather.add(beer);//已在Drinks类中定义数目
            }
            if (drinks instanceof Juice) {
                for (Juice juice : juice_Gather) {
                    if (juice.getname().compareTo(drinks.getname()) == 0) {
                        juice.stock(drinks.getNum());
                        return;
                    }
                }
                Juice juice = (Juice) drinks;
                juice_Gather.add(juice);//已在Drinks类中定义数目
            }
        }


    }
}