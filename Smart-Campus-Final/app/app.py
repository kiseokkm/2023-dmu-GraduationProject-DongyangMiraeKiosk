from flask import Flask, render_template
import requests
from bs4 import BeautifulSoup

app = Flask(__name__)


@app.route('/')
def index():
    url = 'https://www.dongyang.ac.kr/dongyang/130/subview.do'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')
    menu_data = []
    # Extracting date and day information
    date_day_section = soup.find_all('th')
    date_day_data = [item.get_text(strip=True) for item in date_day_section if "." in item.get_text()]
    date_day_data = date_day_data[:5]  # Assuming 5 days are displayed
    for tr in soup.find_all("tr"):
        columns = tr.find_all("td")
        if "교직원식당" in tr.get_text():  # Exclude rows related to 교직원식당
            continue
        if columns and columns[-2].get_text(strip=True) != "-":  # Extracting the second last column data
            menu_data.append(columns[-2].get_text(strip=True).split())

    
    # Distributing the menu items across 5 days
    day_wise_menu_data = {}
    menu_items_per_day = len(menu_data) // 5
    for i, date in enumerate(date_day_data):
        day_wise_menu_data[date] = menu_data[i * menu_items_per_day : (i + 1) * menu_items_per_day]
    
    return render_template('index.html', menu=day_wise_menu_data, dates=date_day_data)


if __name__ == '__main__':
    app.run(debug=True)
@app.route('/week/<direction>')
def week_data(direction):
    today = datetime.date.today()
    weekday = today.weekday()
    start_of_week = today - datetime.timedelta(days=weekday)
    
    if direction == 'prev':
        start_of_week -= datetime.timedelta(days=7)
    elif direction == 'next':
        start_of_week += datetime.timedelta(days=7)
    
    # Now, we'd typically fetch the menu data based on start_of_week
    # For now, we'll just return the start date of the week as a demonstration
    return str(start_of_week)


@app.route('/previous-week')
def previous_week():
    # TODO: Adjust the URL or parameters to get the data for the previous week
    url = "https://www.dongyang.ac.kr/dongyang/130/subview.do"
    response = requests.get(url)
    soup = BeautifulSoup(response.content, 'html.parser')
    table = soup.find("table")
    menu_data = []
    for tr in table.find_all("tr"):
        columns = tr.find_all("td")
        day_column = tr.find("th")
        if day_column and "동양미래푸드" in tr.get_text():
            date_day = day_column.get_text(strip=True)
            date, day = date_day.split(" ")[0], date_day.split(" ")[2]
            menu = columns[2].get_text(strip=True)
            menu_data.append((date, day, menu))
    return jsonify(menu_data)



@app.route('/next-week')
def next_week():
    # TODO: Adjust the URL or parameters to get the data for the next week
    url = "https://www.dongyang.ac.kr/dongyang/130/subview.do"
    response = requests.get(url)
    soup = BeautifulSoup(response.content, 'html.parser')
    table = soup.find("table")
    menu_data = []
    for tr in table.find_all("tr"):
        columns = tr.find_all("td")
        day_column = tr.find("th")
        if day_column and "동양미래푸드" in tr.get_text():
            date_day = day_column.get_text(strip=True)
            date, day = date_day.split(" ")[0], date_day.split(" ")[2]
            menu = columns[2].get_text(strip=True)
            menu_data.append((date, day, menu))
    return jsonify(menu_data)

