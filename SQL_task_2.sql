Используем общедоступный ресурс:
Решить задачу, выполнить запрос и предоставить текст запроса и  скриншот с запросом и результатом выполнения.

https://www.w3schools.com/sql/trysql.asp?filename=trysql_op_in
1) Составьте запрос, который выведет Кастомеров, для которых нет заказов.

SELECT c.CustomerID, c.CustomerName FROM Customers c
WHERE c.CustomerID NOT IN (
	SELECT o.CustomerID FROM Orders o
)

2) Посчитать количество продуктов в каждом заказе и вывести максимальное число продуктов среди всех заказов.

SELECT OrderID, Count(OrderID) as Counter FROM OrderDetails
GROUP BY OrderID

SELECT OrderID, Count(OrderID) as Counter FROM OrderDetails
GROUP BY OrderID
ORDER BY Counter DESC

3) Выбрать самого молодого сотрудника, родившегося в 50-х годах.

SELECT * FROM [Employees]
WHERE strftime('%Y', BirthDate) >= '1950' and strftime('%Y', BirthDate) < '1960'
ORDER BY BirthDate DESC
LIMIT 1

4) Посчитать количество кастомеров, которые заказывали продукты, поставляемые из Великобритании и Испании
	 

SELECT COUNT(DISTINCT CustomerID) FROM [Orders]
JOIN OrderDetails ON Orders.OrderID = OrderDetails.OrderID
JOIN Products ON Products.ProductID = OrderDetails.ProductID
JOIN Suppliers ON Suppliers.SupplierID = Products.SupplierID
WHERE Suppliers.Country = 'UK' OR Suppliers.Country = 'Spain'

5) Вывести сотрудников таким образом, чтобы сотрудник Dodsworth 	Anne 	присутствовал дважды.
Для полученного результата написать запрос, который подсветит наличие дубликатов - выведет дублирующиеся строки - Анну в нашем случае.

SELECT * FROM [Employees]
WHERE LastName = 'Dodsworth' AND FirstName = 'Anne'
UNION ALL
SELECT * FROM [Employees]
ORDER BY EmployeeID

-- highlight 'Dodsworth' 'Anne'
SELECT *, COUNT(EmployeeID) as NUM FROM 
(SELECT * FROM [Employees]
WHERE LastName = 'Dodsworth' AND FirstName = 'Anne'
UNION ALL
SELECT * FROM [Employees]
ORDER BY EmployeeID)
GROUP BY EmployeeID
ORDER BY NUM DESC

6) Написать запрос, который сравнит количество символов в колонке Country из 
Таблицы поставщиков (Suppliers) и кастомеров (Customers)

Дополнение к 6-ой задаче
Написать запрос, который сравнит количество символов в колонке Country из Таблицы поставщиков и кастомеров. 
Добавьте еще одну колонку Result. 
Если количество символов в колонках совпадает, то тогда значение в колонке Result 'Y', если не совпадает, то 'N'

SELECT
CASE 
	WHEN  
		(SELECT SUM(L) FROM (
			SELECT Length(Country) AS L FROM [Suppliers]))
			!=
		(SELECT SUM(L) FROM (
			SELECT Length(Country) AS L FROM [Customers]))
			THEN 'N'
	ELSE 'Y'
END AS 'Result'


7) В таблице Suppliers сгруппируйте поставщиков (SupplierName) по первой букве и выведите, какое количество поставщиков приходится на каждую букву. Полученные строки отсортируйте в алфавитном порядке. Результат работы запроса должен иметь приблизительно такой вид:
A - 2 
C - 3 
D -1 

SELECT (FirstLetter || '-' || COUNT(*)) AS Count FROM (
	SELECT SUBSTR(SupplierName, 1, 1) AS FirstLetter 
	FROM [Suppliers]
)
GROUP BY FirstLetter

8) Вывести кастомеров (customerid, customername), у которых самый высокий по стоимости товар в заказе 
давайте чуть изменим задание, точнее дополним. 
найдите кастомеров, у которых либо самый высокий товар по стоимости, либо второй по стоимости

SELECT Customers.CustomerID, Customers.CustomerName, Price FROM [Customers]
JOIN Orders ON Orders.CustomerID = Customers.CustomerID
JOIN OrderDetails ON OrderDetails.OrderID = Orders.OrderID
JOIN Products ON Products.ProductID = OrderDetails.ProductID
WHERE Price IN (SELECT Price FROM Products ORDER BY Price DESC LIMIT 2)
ORDER BY Price DESC


