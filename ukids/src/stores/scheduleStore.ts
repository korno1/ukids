import { create } from 'zustand';

interface Store {
  events: Event[];
  setEvents: (events: Event[]) => void;
  selectedDate: string | null;
  setSelectedDate: (date: string | null) => void;
  eventData: Event[] | null;
  setEventData: (events: Event[] | null) => void;
  showScheduleList: boolean;
  setShowScheduleList: (show: boolean) => void;
}

interface Event {
  id: string;
  title: string;
  allDay: boolean;
  start: string;
  end?: string;
  color: string;
  textColor: string;
  extendedProps: {
    place?: string;
    family: string;
  };
}

export const useScheduleStore = create<Store>((set) => ({
  events: [],
  setEvents: (events) => set({ events }),
  selectedDate: null,
  setSelectedDate: (date) => set({ selectedDate: date }),
  eventData: null,
  setEventData: (events) => set({ eventData: events }),
  showScheduleList: true,
  setShowScheduleList: (show) => set({ showScheduleList: show }),
}));
