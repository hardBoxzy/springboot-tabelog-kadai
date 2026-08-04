let maxDate = new Date();
maxDate = maxDate.setMonth(maxDate.getMonth() + 3);

flatpickr('#fromCheckinDateToCheckoutDate', {
// mode: "range",
	 locale: 'ja',
	minDate: 'today',
	 maxDate: maxDate,
    enableTime: true,
    dateFormat: "Y-m-d H:i",
    
     // ▼ 火曜日(2)と土曜日(6)を選択不可にする設定を追加
    disable: [
		
		
        function(date) {
            // date.getDay() は 0:日, 1:月, 2:火, 3:水, 4:木, 5:金, 6:土
			//            return (date.getDay() === 2 || date.getDay() === 6);
			return holidays.includes(date.getDay());
        }
    ]
});
//    // ▼ カレンダーのHTMLが作られるときに実行される関数を追加
//    onDayCreate: function(_, __, ___, dayElem) {
//        // dayElem.dateObj にそのマスの日付データが入っています
//        const currentDay = dayElem.dateObj.getDay();
//
//        // もしそのマスの曜日が、除外曜日の配列に含まれていたら
//        if (disabledDaysFromServer.includes(currentDay)) {
//            // HTML要素に title 属性を設定（カーソルを置くと文字が出るようになります）
//            dayElem.setAttribute('title', '定休日のため選択できません');
//        }
//    }
