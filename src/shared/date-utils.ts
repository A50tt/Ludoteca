export class DateUtils {
    public static formatDate(date: Date | string): string {
        const d = new Date(date); // Ensure it's a Date object
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
        const day = String(d.getDate()).padStart(2, '0');
        console.log(`${year}-${month}-${day}`);
        return `${year}-${month}-${day}`;
    }
}