export class DateUtils {
    public static formatDate(date: Date | string): string {
        const d = new Date(date); // Ensure it's a Date object
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
        const day = String(d.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    public static getDaysDifference(startDate: any, endDate: any): number {
        // Handle Moment objects or convert to Date
        const start = startDate?.toDate ? startDate.toDate() : new Date(startDate);
        const end = endDate?.toDate ? endDate.toDate() : new Date(endDate);
        const timeDifferenceMs = end.getTime() - start.getTime();
        return Math.ceil(timeDifferenceMs / (1000 * 60 * 60 * 24));
    }

    public static addDays(date: any, days: number): Date {
        // Handle Moment objects or convert to Date
        const result = date?.toDate ? date.toDate() : new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }
}