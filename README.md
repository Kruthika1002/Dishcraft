
# 🍽️ DishCraft

**DishCraft** is a Java-based desktop application that helps users discover recipes based on available ingredients. It reduces food wastage and improves cooking efficiency by providing smart suggestions from a locally stored recipe database.

---

## 👩‍💻 Project Contributors
- K. Aasrika (1RVU23CSE203)
- Kruthika S (1RVU23CSE228)  
Under the guidance of **Prof. Ritesh Nune**  
School of Computer Science and Engineering, RV University, Bangalore

---

## 🧠 Problem Statement

Often, users struggle to decide what to cook with the ingredients they currently have. DishCraft solves this by:
- Taking a list of available ingredients.
- Comparing them with a recipe database.
- Suggesting dishes that match the ingredients.
- Highlighting missing ingredients.
- Allowing users to add new recipes with ease.

---

## 🛠️ Features

- 💡 **Real-time ingredient matching** for recipe suggestions.
- 📋 **Easy-to-use GUI** built with Java Swing.
- ➕ **Add new recipes** via a custom input dialog.
- 📁 **Persistent recipe storage** using a CSV file.
- 🎨 **Visually styled panels** with background images and clean design.

---

## 📂 File Structure

```bash
DishCraft/
├── Dishcraft.java         # Main Java source file
├── recipes.csv            # CSV file with ingredients, recipe names, and instructions
├── dishcraft_bg.jpg       # Background image (optional)
└── README.md              # This file
```

---

## 💻 How to Run

1. **Compile the Java file:**

```bash
javac Dishcraft.java
```

2. **Run the application:**

```bash
java Dishcraft
```

> Make sure `recipes.csv` is present in the same directory.

---

## 📋 Sample CSV Format (recipes.csv)

```
ingredients;name;instructions
rice,water,salt;Rice;Wash rice thoroughly, add water, and cook
eggs,butter,salt;Scrambled Eggs;Beat eggs, heat butter, stir until cooked
```

---

## 📈 Future Enhancements

- Ingredient substitution suggestions.
- Meal planning and calendar integration.
- Nutrition tracking.
- Cloud syncing and user profiles.
- Mobile app version.

---

## 📸 Screenshots

<div style="display: flex; justify-content: space-between;">
<img src="https://github.com/user-attachments/assets/2132c635-a29d-4f9a-b22c-39c359dfcd98)" width="50%" />
</div>
---

## 📜 License

This project is part of the **CS2024 Mini Project** for educational purposes only.

---
