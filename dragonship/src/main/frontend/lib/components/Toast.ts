import {Options, toast, ToastType} from "bulma-toast";
import 'animate.css';

export function showToast(message: string, typeToast: ToastType, duration: number = 2000, extraOptions: Options | undefined = undefined): void {
    toast({
        message: (extraOptions?.message ? extraOptions.message : message),
        type: typeToast,
        position: 'bottom-right',
        duration: duration,
        animate: {in: 'fadeIn', out: 'fadeOut'},
        ...(extraOptions ? extraOptions : {})
    })
}
