const columnDefs = [
    {field: 'athlete'},
    {field: 'age'},
    {field: 'country'},
    {field: 'year'},
    {field: 'date'},
    {field: 'sport'},
    {field: 'gold'},
    {field: 'silver'},
    {field: 'bronze'},
    {field: 'total'}
];

// https://raw.githubusercontent.com/ag-grid/ag-grid/master/packages/ag-grid-docs/src/olympicWinnersSmall.json

function createTableHTML(d, caption, options) {
    caption = caption || '';
    options = options || {};
    const rowLength = _.get(options, 'rowLength', 0);
    const sort = _.get(options, 'sort', false);
    let rowData = _.cloneDeep(d.rowData);
    if (sort) {
        rowData.sort((a, b) => b[sort] - a[sort]);
    }
    if (rowLength) {
        rowData = rowData.slice(0, rowLength);
    }
    const columnDefs = d.columnDefs;
    const rows = [];
    function getChildCount(d, count) {
        if (d.children && !d.children[0].children) {
            return d.children.length;
        }
        if (d.children) {
            d.children.forEach(child => {
                count = count + getChildCount(child, count);
            })
        }
        return count;
    }
    let headerRow = columnDefs.reduce( (line, col, index) => {
       const childCount = getChildCount(col, 0);
       const colSpan = childCount ? ' colspan=' + childCount : '';
       const cellStyle = (!childCount && index) ? ' class="right" ': '';
       const colTitle = col.headerName || _.capitalize(col.field) || '';
       line = line + `<th ${cellStyle} ${colSpan}>${colTitle}</th>`;
       return line;
    }, '');
    rows.push('<thead><tr>' + headerRow + '</tr></thead><tbody>');
    function addDataRow(rowHTML, cols, row, rowIndex, options, baseField, ci) {
        cols.forEach((col, colIndex) => {
           if (col.children) {
               debugger;
               rowHTML = addDataRow(rowHTML, col.children, row, rowIndex, options, baseField, colIndex);
           } else {
               let item = row[col.field];
               item = item || '';
               rowHTML += `<td>${item}</td>`;
           }
        });
        return rowHTML;
    }
    function getBaseField(cols) {
        if (cols[0].children) {
            return getBaseField(cols[0].children);
        }
        return cols[0].field;
    }
    rowData.forEach((row, counter) => {
        const rowHTML = addDataRow('', columnDefs, row, counter, options, getBaseField(columnDefs));
        let rowStyle = '';
        rows.push(`<tr ${rowStyle}>${rowHTML}</tr>`);
    });
    const banded = (options && options.banded) ? " banded " : '';
    let html = `<table class="data-table ${banded}">${caption}`
                   + rows.join('');
    html = html + '</tbody></table>';
    return html;
}

const tableHTML = createTableHTML({columnDefs, rowData});
document.getElementById('myGrid').innerHTML = tableHTML;
