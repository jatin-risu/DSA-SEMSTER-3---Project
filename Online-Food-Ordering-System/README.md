# Online Food Ordering System (Java)

A console-based food ordering system built as a 4th semester Java project. It demonstrates core Java collections and sorting.

## Features
- View the restaurant menu
- Sort the menu by price, name or rating
- Add items to a cart and view the bill
- Undo the last item added to the cart
- Place orders and process them in the order they arrive
- View pending orders and completed order history

## Data Structures Used

| Concept | Where it is used |
|---------|------------------|
| `ArrayList` | Stores the menu items and the cart |
| `Stack` | Undo last cart action; order history (latest order on top) |
| `Queue` (`LinkedList`) | Pending orders, served first-come-first-served (FIFO) |
| Sorting | Bubble sort by price; `Collections.sort()` with `Comparator` for name, rating, price (desc) |

## Project Structure
```
Online-Food-Ordering-System/
├── src/
│   ├── FoodOrderingSystem.java   (main class)
│   ├── MenuItem.java
│   ├── CartItem.java
│   └── Order.java
├── .gitignore
└── README.md
```

## How to Run

Requires JDK 8 or higher.

```bash
cd Online-Food-Ordering-System
javac -d out src/*.java
java -cp out FoodOrderingSystem
```

## Sample Flow
1. Choose `3` to add items to the cart
2. Choose `4` to undo a mistake
3. Choose `6` to place the order (goes into the queue)
4. Choose `7` to process the next order (moves to history stack)
5. Choose `9` to see order history

## Author
Your Name | Your College | 4th Semester
