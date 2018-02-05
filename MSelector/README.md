# MSelector

MSelector is a selector for mobile device.

## install
```html
<link rel="stylesheet" href="./css/mselector.css">
<script type="text/javascript" src="./js/mselector.js"></script>
```
## example
```js
var selector = new MSelector({
    onselected: function(data){
        alert('selected: ' + data);
        this.hide();
    }
});

selector.loadContent([[
    {code: 'f1', value: 'Apple'},
    {code: 'f2', value: 'banana'}
]]);

selector.show();
```

## API
<table>
<thead style="width: 100%">
<tr>
<th>properties</th>
<th>type</th><th>parameters</th><th>description</th>
</tr>
<colgroup>
<col width="20%" />
<col width="20%" />
<col width="20%" />
<col width="40%" />
</colgroup>
</thead>
<tbody>
<tr><td>columnNum</td><td>number</td><td></td><td>the column number</td></tr>
<tr><td>columnStyles</td><td>array</td><td></td><td>the style for each column, example: ['width:34%;text-align:right;','width:33%;','width:33%;text-align:left;']</td>
<tr><td>show</td><td>function</td><td></td><td>show selector</td></tr>
<tr><td>hide</td><td>function</td><td></td><td>hide selector</td></tr>
<tr><td>onselected</td><td>function</td><td>data</td><td>trigger when click comfirm button</td></tr>
<tr><td>oncolumnchange</td><td>function</td><td>colIdx,itemIdx,data</td><td>trigger when item selected</td></tr>
<tr><td>showWaiting</td><td>function</td><td></td><td>show waiting</td></tr>
<tr><td>hideWaiting</td><td>function</td><td></td><td>hide waiting</td></tr>
<tr><td>loadContent</td><td>function</td><td>array</td><td>load content data, example: selector.loadContent([[
    {code: 'f1', value: 'Apple'},
    {code: 'f2', value: 'banana'}
]])</td></tr>
<tr><td>loadColumn</td><td>function</td><td>colIdx,data</td><td>load content data for column, example: selector.loadColumn(0, [
    {code: 'f1', value: 'Apple'},
    {code: 'f2', value: 'banana'}
])</td></tr>
</tbody>
</table>

## about
contact us with futurespeed@126.com