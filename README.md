# HypeMart Inventory Management System

HypeMart 🛒 is a large hypothetical supermarket store based in Sydney, Australia that sells foods and household goods. 
This desktop application is an **Inventory Management Application** that uses JDK11 with OpenJFX11 as a GUI platform and SQLite as a data storage platform. 


## How to use this Application


### 1. 🔑 Login using a predefined username and password 🔑
Depending on the type of the user credentials entered into the login screen, the user will be given permission to access different screens depending on their user type. 
#### 🧑User Login👩
|User Type  |Full Name|Username  |Password | Store/Supplier ID| Supplier Name|
|--|--| --|--|--|--|
| Store Employee (store manager)|Adam Lee| adam02  |dogs  |ST01| -|
| Store Employee (store manager)|Trent Ca| trent03|frogs|ST02| -|
| Supplier Employee |Ryan Da|bob04|pears|S001| woolies|
| Supplier Employee |Lucas Tran|lucas05|apples|S002| koles|
| Supplier Employee |Jenny Smith|jenny06|cat|S003| westfarms|
| Supplier Employee |Marc Sen|marc01|sunny|S001| woolies|


#### 🔏Access Permissions🔏
|User Type| Accessible screens  |
|--|--|
| Store Employee | *Dashboard, Suppliers, My Orders, New Order, About Us* |
| Supplier Employee | *Order Status, About Us* |


### 2. 🎉Use the application!🎉
The application does not require any additional actions for it to work!
* Note: Settings and help buttons on the navigation pane are non-functional (heuristic purposes)
* Note: Ensure that suppliers for orders must exist in the database
* Note: There are 4 order statuses: Order Placed, In Transit, Delivered, Cancelled
* Note: The products that each supplier sells are: banana, toilet paper, bread, milk, pasta, panadol, detergent, dog treats.
* Note: A store employee can only view the orders for their own store (security purposes)

## 🔎Using the search bar in the Dashboard🔎
* When searching for orders based on their product or status, the user's input must be spelt correctly. However, the input is case insensitive.

## 💰Prices💰
* The prices of all products and the total price of an order are displayed (without decimal points) in cents.

## 💢Assumptions💢
1. An order only contains products from one supplier
2. All suppliers sell the same products
3. Orders can only be deleted if they have not been delivered
4. All store employees using the inventory system are store managers

## 🔍Class Diagram🔎
![Imgur](https://i.imgur.com/34R0z8i.png)

## 🛒Application Screenshots🛒
Login Screen
![enter image description here](https://i.imgur.com/8mSQ7xy.png)

Dashboard
![Imgur](https://i.imgur.com/O4xM1gu.png)

Suppliers
![Imgur](https://i.imgur.com/6ivwLdB.png)

New Order
![Imgur](https://i.imgur.com/6c6N9Oq.png)
![Imgur](https://i.imgur.com/M3yI6rI.png)

Order Confirmation
![Imgur](https://i.imgur.com/mXQWHKd.png)

My Orders
![Imgur](https://i.imgur.com/JEzIUWV.png)

Edit an existing order (accessed through My Orders screen)
![Imgur](https://i.imgur.com/XwczabO.png)

View and Update Order Status (Supplier Employee Only)
![Imgur](https://i.imgur.com/LS0kJEA.png)

About Us
![Imgur](https://i.imgur.com/UhDsev8.png)

## References for Code
These websites were used to inspire some of the code we wrote to build this inventory management system
  * Searchable tables using a filtered list object: https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
  * Interactive pie chart: https://stackoverflow.com/questions/28943599/show-pies-percentage-of-piechart
  * Table designs: https://edencoding.com/style-tableview-javafx/ & https://blog.ngopal.com.np/2012/07/11/customize-scrollbar-via-css/


